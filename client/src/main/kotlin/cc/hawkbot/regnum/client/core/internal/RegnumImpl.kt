/*
 * Regnum - A Discord bot clustering system made for Hawk
 *
 * Copyright (C) 2019  Michael Rittmeister
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package cc.hawkbot.regnum.client.core.internal

import cc.hawkbot.regnum.client.Feature
import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.command.CommandParser
import cc.hawkbot.regnum.client.command.impl.CommandParserImpl
import cc.hawkbot.regnum.client.command.permission.PermissionManager
import cc.hawkbot.regnum.client.command.permission.PermissionManagerImpl
import cc.hawkbot.regnum.client.command.translation.LanguageManager
import cc.hawkbot.regnum.client.commands.general.HelpCommand
import cc.hawkbot.regnum.client.commands.settings.*
import cc.hawkbot.regnum.client.config.CassandraConfig
import cc.hawkbot.regnum.client.config.CommandConfig
import cc.hawkbot.regnum.client.config.GameAnimatorConfig
import cc.hawkbot.regnum.client.config.ServerConfig
import cc.hawkbot.regnum.client.core.MessageCache
import cc.hawkbot.regnum.client.core.discord.Discord
import cc.hawkbot.regnum.client.core.discord.MessageWatcher
import cc.hawkbot.regnum.client.entities.RegnumGuild
import cc.hawkbot.regnum.client.entities.RegnumUser
import cc.hawkbot.regnum.client.entities.cache.CassandraCache
import cc.hawkbot.regnum.client.entities.cache.impl.CassandraCacheImpl
import cc.hawkbot.regnum.client.entities.permission.PermissionNode
import cc.hawkbot.regnum.client.util._setRegnum
import cc.hawkbot.regnum.entities.cassandra.CassandraEntity
import cc.hawkbot.regnum.io.database.CassandraSource
import cc.hawkbot.regnum.util.logging.Logger
import cc.hawkbot.regnum.waiter.impl.EventWaiter
import cc.hawkbot.regnum.waiter.impl.EventWaiterImpl
import com.datastax.driver.extras.codecs.enums.EnumNameCodec
import net.dv8tion.jda.api.hooks.IEventManager

/**
 * Implementation of [Regnum]
 * @param serverConfig the configuration for server specific settings
 * @property eventManager the event manager
 * @property token the Regnum token
 * @property gameAnimatorConfig the config used by the game anitmator
 * @param commandConfig the config for command related settings
 * @see cc.hawkbot.regnum.client.RegnumBuilder
 * @constructor Constructs a new Regnum instance
 */
@Suppress("FunctionName", "ProtectedInFinal", "LeakingThis")
open class RegnumImpl(
        serverConfig: ServerConfig,
        override val eventManager: IEventManager,
        val gameAnimatorConfig: GameAnimatorConfig,
        commandConfig: CommandConfig,
        override val disabledFeatures: List<Feature>,
        private val _messageCache: MessageCache
) : Regnum {

    private val log = Logger.getLogger()
    override val token: String = serverConfig.token
    override val languageManager: LanguageManager = commandConfig.languageManager
    override val owners: List<Long> = commandConfig.botOwners
    override lateinit var websocket: WebsocketImpl
    override lateinit var discord: Discord
    override lateinit var commandParser: CommandParser
    override lateinit var cassandra: CassandraSource
    override lateinit var guildCache: CassandraCache<RegnumGuild>
    override lateinit var userCache: CassandraCache<RegnumUser>
    override lateinit var eventWaiter: EventWaiter
    private lateinit var _permissionManager: PermissionManager
    lateinit var metricsSender: MetricsSender
    override var permissionManager: PermissionManager
        get() = _getPermissionManager()
        set(value) = _setPermissionManager(value)
    override val messageCache: MessageCache
        get() = _getMessageCache()

    /**
     * Initializes the Regnum client.
     * @param serverConfig the server configuration
     * @param cassandraConfig the cassandra Configuration
     * @param commandConfig the command configuration
     */
    fun init(serverConfig: ServerConfig, cassandraConfig: CassandraConfig, commandConfig: CommandConfig): Regnum {
        _setRegnum(this)
        events()
        cassandra(cassandraConfig)
        caches()
        commandManager(commandConfig)
        websocket(serverConfig)
        return this
    }

    protected fun events() {
        eventWaiter = EventWaiterImpl(eventManager)
        if (Feature.MESSAGE_CACHE !in disabledFeatures) {
            eventManager.register(MessageWatcher(this))
        }
    }

    protected fun cassandra(cassandraConfig: CassandraConfig) {
        val defaultDatabases = cassandraConfig.defaultDatabases
        // Default databases
        val generators = defaultDatabases.toMutableList()
        generators.add("CREATE TABLE IF NOT EXISTS ${CassandraEntity.TABLE_PREFIX}guilds(" +
                "id BIGINT," +
                "prefix TEXT," +
                "language_tag TEXT," +
                "blacklisted_channels LIST<BIGINT>," +
                "whitelisted_channels LIST<BIGINT>," +
                "PRIMARY KEY (id)" +
                ");")
        if (Feature.PERMISSION_SYSTEM !in disabledFeatures) {
            generators.add("CREATE TABLE IF NOT EXISTS ${CassandraEntity.TABLE_PREFIX}permissions(" +
                    "id BIGINT," +
                    "negated BOOLEAN," +
                    "guild_id BIGINT," +
                    "permission_node TEXT," +
                    "type TEXT," +
                    "PRIMARY KEY (id, guild_id, permission_node, type)" +
                    ");")
        }
        generators.add("CREATE TABLE IF NOT EXISTS ${CassandraEntity.TABLE_PREFIX}user(" +
                "id BIGINT," +
                "language_tag TEXT," +
                "PRIMARY KEY (id)" +
                ");")
        cassandra = CassandraSource(cassandraConfig.username, cassandraConfig.password, cassandraConfig.keyspace, cassandraConfig.codecRegistry, cassandraConfig.contactPoints)
        cassandra.codecRegistry.register(EnumNameCodec(PermissionNode.PermissionTarget::class.java))
        val source = cassandra.connect()
        log.info("[Regnum] Successfully connected to Cassandra cluster")
        log.info("[Regnum] Generating Cassandra databases")
        generators.forEach {
            val statement = source.session.prepare(it).bind()
            source.session.executeAsync(statement).get()
        }
        log.info("[Regnum] Generated databases. Initializing caches")
    }

    protected fun caches() {
        guildCache = CassandraCacheImpl(this, RegnumGuild::class, RegnumGuild.Accessor::class.java)
        userCache = CassandraCacheImpl(this, RegnumUser::class, RegnumUser.Accessor::class.java)
    }

    protected fun commandManager(commandConfig: CommandConfig) {
        val permissionProvider = commandConfig.permissionProvider
        permissionProvider.regnum = this
        commandParser = CommandParserImpl(commandConfig, commandConfig.botOwners, this)
        commandParser.registerCommands(*commandConfig.commands.toTypedArray())
        eventManager.register(commandParser)
        languageManager.regnum(this)

        // Default commands
        commandParser.registerCommands(PrefixCommand(), PermissionCommand(), LanguageCommand(), HelpCommand(), BlacklistCommand(), WhitelistCommand())

        // Permissions
        if (Feature.PERMISSION_SYSTEM !in disabledFeatures) {
            permissionManager = PermissionManagerImpl(this)
        }
    }

    private fun websocket(serverConfig: ServerConfig) {
        eventManager.register(PacketHandler(this))
        websocket = WebsocketImpl(serverConfig.host, this)
        metricsSender = MetricsSender(this)
        log.info("[Regnum] Connecting to server")
        websocket.start()
    }

    internal fun discordInitialized(): Boolean {
        return this::discord.isInitialized
    }

    private fun _getPermissionManager(): PermissionManager {
        if (Feature.PERMISSION_SYSTEM !in disabledFeatures) {
            throw IllegalStateException("You have to enable Feature.PERMISSION_SYSTEM in order to use that feature.")
        }
        return permissionManager
    }

    private fun _setPermissionManager(value: PermissionManager) {
        this._permissionManager = value
    }

    private fun _getMessageCache(): MessageCache {
        if (Feature.MESSAGE_CACHE in disabledFeatures) {
            throw UnsupportedOperationException("Message cache is disabled!")
        }
        return _messageCache
    }
}