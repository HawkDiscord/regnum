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

package cc.hawkbot.regnum.server.plugin

import cc.hawkbot.regnum.entites.Payload
import cc.hawkbot.regnum.entites.packets.MetricsPacket
import cc.hawkbot.regnum.server.plugin.entities.Node
import io.javalin.websocket.WsSession

/**
 * Representation of the Regnum websocket.
 */
@Suppress("unused")
interface Websocket {

    /**
     * A list containing all available nodes
     */
    val nodes: List<Node>

    fun getNode(session: WsSession): Node {
        return nodes.first { it.session == session }
    }

    /**
     * Sends a [message] to the specified [session]
     * @param session the session to sent the message to
     * @param message the message
     */
    fun send(session: WsSession, message: String)

    /**
     * Sends a [payload] to the specified [session]
     * @param session the session to sent the message to
     * @param payload the payload
     */
    fun send(session: WsSession, payload: Payload) {
        send(session, payload.toJson())
    }

    /**
     * Returns the las received [MetricsPacket] of a the provided [node].
     */
    fun metrics(node: Node): MetricsPacket

}