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

package cc.hawkbot.regnum.server.plugin.entities

import cc.hawkbot.regnum.entites.Payload
import cc.hawkbot.regnum.server.plugin.Websocket
import io.javalin.websocket.WsSession

/**
 * Representation of a connected Regnum node.
 */
@Suppress("unused")
interface Node {

    /**
     * The websocket session.
     */
    val session: WsSession

    /**
     * The range of all shards on this node.
     */
    val shards: IntRange

    /**
     * The nodes pulse.
     */
    val pulse: Pulse

    /**
     * The current websocket instance.
     */
    val websocket: Websocket

    /**
     * Sends a [message] to this node
     * @param message the message
     */
    fun send(message: String) {
        websocket.send(session, message)
    }

    /**
     * Sends a [payload] to this node
     * @param payload the payload
     */
    fun send(payload: Payload) {
        websocket.send(session, payload)
    }
}