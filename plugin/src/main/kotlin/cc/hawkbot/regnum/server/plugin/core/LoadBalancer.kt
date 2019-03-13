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

package cc.hawkbot.regnum.server.plugin.core

import cc.hawkbot.regnum.server.plugin.events.websocket.WebSocketCloseEvent
import cc.hawkbot.regnum.server.plugin.events.websocket.WebsocketAuthorizedEvent

/**
 * Represents balancing of shards to different Regnum nodes.
 */
interface LoadBalancer {

    /**
     * Count of optimal shards.
     */
    val optimalShards: Int

    /**
     * The Discord Bot token.
     */
    val token: String

    /**
     * Method that handles new connections.
     * @param event the authorization event
     */
    fun handleConnect(event: WebsocketAuthorizedEvent)

    /**
     * Method that handles disconnects.
     * @param event the disconnect event
     */
    fun handleDisconnect(event: WebSocketCloseEvent)

    /**
     * Method that calculated shard ids.
     * @param to the maximal shard id
     */
    fun shardIds(to: Int = optimalShards): IntRange {
        return 0 until to
    }
}