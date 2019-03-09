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

import cc.hawkbot.regnum.server.discord.DiscordBotImpl
import cc.hawkbot.regnum.server.plugin.Server
import cc.hawkbot.regnum.server.plugin.Websocket
import cc.hawkbot.regnum.server.plugin.discord.DiscordBot
import cc.hawkbot.regnum.server.plugin.io.config.Config
import io.javalin.Javalin

class ServerImpl(
        override val launchedAt: Long,
        override val dev: Boolean
) : Server {
    override val config: Config = Config("config/server.yml")
    override val javalin: Javalin = Javalin.create().start(config.getInt(Config.SOCKET_PORT))
    override lateinit var websocket: Websocket
    override lateinit var discordBot: DiscordBot

    private lateinit var pluginManager: PluginManager

    init {
        shutdownHook()
        plugins()
        initWebsocket()
        initDiscord()
    }

    private fun plugins() {
        pluginManager = PluginManager(this)
    }

    private fun shutdownHook() {
        Runtime.getRuntime().addShutdownHook(Thread {
            close()
        })
    }

    private fun initWebsocket() {
        javalin.ws("/ws") {
            websocket = WebsocketImpl(it, config, this)
        }
    }

    private fun initDiscord() {
        discordBot = DiscordBotImpl(config.getString(Config.DISCORD_TOKEN))
    }

    override fun close() {
        pluginManager.close()
        javalin.stop()
        discordBot.close()
    }

}