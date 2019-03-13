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

package cc.hawkbot.regnum.server.plugin.io.config

import cc.hawkbot.regnum.io.config.GenericConfig

/**
 * Config for Regnum server.
 * @param source the path to the config
 * @see GenericConfig
 * @constructor Constructs a new config
 */
class Config(source: String) : GenericConfig(source) {

    companion object {
        // Socket
        const val SOCKET_PORT = "socket.port"
        const val SOCKET_TOKEN = "socket.token"
        const val SOCKET_HEARTBEAT = "socket.heartbeat"
        const val SOCKET_IDENTIFY = "socket.identify_timeout"

        // Discord
        const val DISCORD_TOKEN = "discord.token"
        const val DISCORD_PREFIX = "discord.prefix"
        const val DISCORD_OWNERS = "discord.owners"
        const val DISCORD_NODE_TOKEN = "discord.node_token"

        // General
        const val PLUGINS_DIRECTORY = "general.plugins_directory"
        const val LOAD_BALANCE_TIMEOUT = "general.load_balance_timeout"

        // Sentry
        const val SENTRY_DSN = "sentry.dsn"
    }

    override fun defaults() {
        applyDefault(SOCKET_PORT, 7000)
        applyDefault(SOCKET_TOKEN, "SUPER-SECRET-SOCKET_TOKEN")
        applyDefault(SOCKET_HEARTBEAT, 30)
        applyDefault(SOCKET_IDENTIFY, 30)
        applyDefault(DISCORD_TOKEN, "WUMPUS IS LOVE BRA")
        applyDefault(DISCORD_PREFIX, "hc!")
        applyDefault(DISCORD_OWNERS, listOf(416902379598774273L, 240797338430341120L))
        applyDefault(DISCORD_NODE_TOKEN, "WUMPUS IS STILL LOVE BRA")
        applyDefault(PLUGINS_DIRECTORY, "plugins/")
        applyDefault(LOAD_BALANCE_TIMEOUT, 30)
        applyDefault(SENTRY_DSN, "YOU DSN")
    }
}