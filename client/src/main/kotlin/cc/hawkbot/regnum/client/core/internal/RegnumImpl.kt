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

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.RegnumBuilder
import cc.hawkbot.regnum.client.command.CommandParser
import cc.hawkbot.regnum.client.command.ICommand
import cc.hawkbot.regnum.client.command.impl.CommandParserImpl
import cc.hawkbot.regnum.client.command.permission.IPermissionProvider
import cc.hawkbot.regnum.client.command.permission.PermissionManager
import cc.hawkbot.regnum.client.command.permission.PermissionManagerImpl
import cc.hawkbot.regnum.client.command.translation.LanguageManager
import cc.hawkbot.regnum.client.commands.settings.LanguageCommand
import cc.hawkbot.regnum.client.commands.settings.PermissionCommand
import cc.hawkbot.regnum.client.commands.settings.PrefixCommand
import cc.hawkbot.regnum.client.core.discord.Discord
import cc.hawkbot.regnum.client.core.discord.GameAnimator
import cc.hawkbot.regnum.client.entities.RegnumGuild
import cc.hawkbot.regnum.client.entities.RegnumUser
import cc.hawkbot.regnum.client.entities.cache.CassandraCache
import cc.hawkbot.regnum.client.entities.cache.impl.CassandraCacheImpl
import cc.hawkbot.regnum.client.entities.cassandra.CassandraEntity
import cc.hawkbot.regnum.client.entities.permission.PermissionNode
import cc.hawkbot.regnum.client.io.database.CassandraSource
import cc.hawkbot.regnum.client.util._setRegnum
import cc.hawkbot.regnum.util.logging.Logger
import cc.hawkbot.regnum.waiter.impl.EventWaiter
import cc.hawkbot.regnum.waiter.impl.EventWaiterImpl
import com.datastax.driver.core.CodecRegistry
import com.datastax.driver.extras.codecs.enums.EnumNameCodec
import com.datastax.driver.extras.codecs.enums.EnumOrdinalCodec
import net.dv8tion.jda.api.hooks.IEventManager
import java.util.function.Function

/**
 * Implementation of [Regnum]
 * @param host the host of the Regnum server
 * @property eventManager the event manager
 * @property token the Regnum token
 * @property games a list of games for the [GameAnimator]
 * @property gameAnimatorInterval the interval for the [GameAnimator]
 * @see cc.hawkbot.regnum.client.RegnumBuilder
 * @constructor Constructs a new Regnum instance
 */
class RegnumImpl(
        host: String,
        override val eventManager: IEventManager,
        override val token: String,
        val games: MutableList<GameAnimator.Game>,
        val gameTranslator: Function<String, String>,
        val gameAnimatorInterval: Long,
        permissionProvider: IPermissionProvider,
        defaultPrefix: String,
        commands: List<ICommand>,
        override val owners: List<Long>,
        override val languageManager: LanguageManager,
        codecRegistry: CodecRegistry,
        cassandraKeyspace: String,
        cassandraAuthenticator: RegnumBuilder.CassandraAuthenticator,
        contactPoints: Collection<String>,
        defaultDatabases: Collection<String>
) : Regnum {

    private val log = Logger.getLogger()
    override val websocket: WebsocketImpl
    override lateinit var discord: Discord
    override val commandParser: CommandParser
    override val cassandra: CassandraSource
    override lateinit var guildCache: CassandraCache<RegnumGuild>
    override lateinit var userCache: CassandraCache<RegnumUser>
    override val eventWaiter: EventWaiter
    override lateinit var permissionManager: PermissionManager

    init {
        _setRegnum(this)
        permissionProvider.regnum = this
        commandParser = CommandParserImpl(
                defaultPrefix,
                permissionProvider,
                this
        )
        commandParser.registerCommands(*commands.toTypedArray())
        eventManager.register(PacketHandler(this))
        eventManager.register(commandParser)
        eventWaiter = EventWaiterImpl(eventManager)
        websocket = WebsocketImpl(host, this)
        languageManager.regnum(this)
        // Default databases
        val generators = defaultDatabases.toMutableList()
        generators.add("CREATE TABLE IF NOT EXISTS ${CassandraEntity.TABLE_PREFIX}guilds(" +
                "id BIGINT," +
                "prefix TEXT," +
                "language_tag TEXT," +
                "PRIMARY KEY (id)" +
                ");")
        generators.add("CREATE TABLE IF NOT EXISTS ${CassandraEntity.TABLE_PREFIX}permissions(" +
                "id BIGINT," +
                "negated BOOLEAN," +
                "guild_id BIGINT," +
                "permission_node TEXT," +
                "type TEXT," +
                "PRIMARY KEY (id, guild_id, permission_node, type)" +
                ");")
        generators.add("CREATE TABLE IF NOT EXISTS ${CassandraEntity.TABLE_PREFIX}user(" +
                "id BIGINT," +
                "language_tag TEXT," +
                "PRIMARY KEY (id)" +
                ");")
        cassandra = CassandraSource(cassandraAuthenticator.username, cassandraAuthenticator.password, cassandraKeyspace, codecRegistry, contactPoints)
        cassandra.codecRegistry.register(EnumNameCodec(PermissionNode.PermissionTarget::class.java))
        val source = cassandra.connect()
        log.info("[Regnum] Successfully connected to Cassandra cluster")
        log.info("[Regnum] Generating Cassandra databases")
        generators.forEach {
            try {
                val statement = source.session.prepare(it).bind()
                source.session.executeAsync(statement).get()
            } catch (e: Exception) {
                log.error("[Regnum] Error while generating default database", e)
            }
        }

        log.info("[Regnum] Generated databases. Initializing caches")

        // Caches
        guildCache = CassandraCacheImpl(this, RegnumGuild::class, RegnumGuild.Accessor::class.java)
        userCache = CassandraCacheImpl(this, RegnumUser::class, RegnumUser.Accessor::class.java)

        // Default commands
        commandParser.registerCommands(PrefixCommand(), PermissionCommand(), LanguageCommand())

        // Permissions
        permissionManager = PermissionManagerImpl(this)
        log.info("[Regnum] Connecting to server")
        websocket.start()
    }
}