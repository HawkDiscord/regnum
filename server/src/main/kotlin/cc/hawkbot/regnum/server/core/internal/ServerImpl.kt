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

package cc.hawkbot.regnum.server.core.internal

import cc.hawkbot.regnum.entities.json.Json
import cc.hawkbot.regnum.entities.packets.MetricsPacket
import cc.hawkbot.regnum.server.core.internal.websocket.ConfigAuthorizer
import cc.hawkbot.regnum.server.core.internal.websocket.WebsocketImpl
import cc.hawkbot.regnum.server.discord.DiscordBotImpl
import cc.hawkbot.regnum.server.plugin.Server
import cc.hawkbot.regnum.server.plugin.Websocket
import cc.hawkbot.regnum.server.plugin.core.AuthorizationHandler
import cc.hawkbot.regnum.server.plugin.core.LoadBalancer
import cc.hawkbot.regnum.server.plugin.discord.DiscordBot
import cc.hawkbot.regnum.server.plugin.io.config.Config
import cc.hawkbot.regnum.util.logging.Logger
import cc.hawkbot.regnum.waiter.EventWaiter
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import net.dv8tion.jda.api.hooks.AnnotatedEventManager
import net.dv8tion.jda.api.hooks.IEventManager
import okhttp3.OkHttpClient
import org.apache.commons.lang3.RandomStringUtils

/**
 * Implementation of [Server].
 * @param launchedAt timestamp of the servers launch date
 * @param dev whether the server is operating in dev mode or not
 * @param noDiscord Whether the server should start the discord bot or not
 * @constructor Constructs a new server
 */
class ServerImpl(
        override val launchedAt: Long,
        override val dev: Boolean,
        noDiscord: Boolean
) : Server {
    private val log = Logger.getLogger()

    override val config: Config = Config("config/server.yml")
    override val javalin: Javalin
    override lateinit var websocket: Websocket
    override lateinit var discordBot: DiscordBot
    override val eventManager: IEventManager = AnnotatedEventManager()
    override val eventWaiter: EventWaiter = EventWaiterImpl(eventManager)
    override var authorizationHandler: AuthorizationHandler = ConfigAuthorizer()
    override lateinit var loadBalancer: LoadBalancer
    override val httpClient: OkHttpClient = OkHttpClient()
    private lateinit var pluginManager: PluginManager
    override lateinit var averageMetrics: MetricsPacket

    init {
        JavalinJackson.configure(Json.JACKSON)
        config.load()
        javalin = Javalin.create().start(config.getInt(Config.SOCKET_PORT))
        shutdownHook()
        plugins()
        hashes()
        initWebsocket()
        initDiscord(noDiscord)
    }

    private fun plugins() {
        pluginManager = PluginManager(this)
    }


    private fun shutdownHook() {
        Runtime.getRuntime().addShutdownHook(Thread {
            close()
        })
    }

    private fun hashes() {
        val hashes = mutableListOf<String>()
        for (i in 0 until 40) {
            hashes.add(RandomStringUtils.randomAlphabetic(55))
        }
        log.info("[Launcher] Launch is in progress here are some random tokens {}", hashes.joinToString())
    }

    private fun initWebsocket() {
        javalin.ws("/ws") {
            websocket = WebsocketImpl(it, this)
        }
        loadBalancer = LoadBalancerImpl(this)
        eventManager.register(loadBalancer)
    }

    private fun initDiscord(noDiscord: Boolean) {
        if (!noDiscord) {
            discordBot = DiscordBotImpl(config.get(Config.DISCORD_TOKEN))
        }
    }

    override fun close() {
        pluginManager.close()
        javalin.stop()
        eventWaiter.close()
        if (this::discordBot.isInitialized) {
            discordBot.close()
        }
    }

}