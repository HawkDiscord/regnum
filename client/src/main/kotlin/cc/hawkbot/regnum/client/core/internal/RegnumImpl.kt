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
import cc.hawkbot.regnum.client.config.GameAnimatorConfig
import cc.hawkbot.regnum.client.config.ServerConfig
import cc.hawkbot.regnum.client.core.ClientEventWaiter
import cc.hawkbot.regnum.client.core.Extension
import cc.hawkbot.regnum.client.core.discord.Discord
import cc.hawkbot.regnum.client.core.discord.ShardManager
import cc.hawkbot.regnum.client.event.EventManager
import cc.hawkbot.regnum.util.logging.Logger
import cc.hawkbot.regnum.waiter.EventWaiter
import kotlin.reflect.KClass

/**
 * Implementation of [Regnum]
 * @param serverConfig the configuration for server specific settings
 * @property eventManager the event manager
 * @property token the Regnum token
 * @property gameAnimatorConfig the config used by the game animator
 * @see cc.hawkbot.regnum.client.RegnumBuilder
 * @constructor Constructs a new Regnum instance
 */
@Suppress("FunctionName", "ProtectedInFinal", "LeakingThis")
open class RegnumImpl(
        serverConfig: ServerConfig,
        override val eventManager: EventManager,
        val gameAnimatorConfig: GameAnimatorConfig,
        override val disabledFeatures: List<Feature>,
        val shardManagerClass: KClass<out ShardManager>,
        extensionsClasses: List<Class<out Extension>>
) : Regnum {

    private val log = Logger.getLogger()
    override val token: String = serverConfig.token
    override lateinit var websocket: WebsocketImpl
    override lateinit var discord: Discord
    override lateinit var eventWaiter: EventWaiter
    lateinit var metricsSender: MetricsSender

    init {
        val extensions = extensionsClasses.map { it.constructors.first().newInstance(this) as Extension }
        extensions.forEach(Extension::initializing)
        events()
        websocket(serverConfig)
        extensions.forEach(Extension::initialized)
    }

    protected fun events() {
        eventWaiter = ClientEventWaiter(eventManager)
    }


    private fun websocket(serverConfig: ServerConfig) {
        websocket = WebsocketImpl(serverConfig.host, this)
        metricsSender = MetricsSender(this)
        log.info("[Regnum] Connecting to server")
        websocket.start()
    }

    internal fun discordInitialized() = this::discord.isInitialized
}