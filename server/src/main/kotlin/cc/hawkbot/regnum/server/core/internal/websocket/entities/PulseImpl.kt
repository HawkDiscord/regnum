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

package cc.hawkbot.regnum.server.core.internal.websocket.entities

import cc.hawkbot.regnum.entities.Payload
import cc.hawkbot.regnum.entities.packets.HeartBeatAckPacket
import cc.hawkbot.regnum.server.plugin.Server
import cc.hawkbot.regnum.server.plugin.entities.Node
import cc.hawkbot.regnum.server.plugin.entities.Pulse
import cc.hawkbot.regnum.server.plugin.events.websocket.WebSocketMessageEvent
import cc.hawkbot.regnum.server.plugin.io.config.Config
import cc.hawkbot.regnum.util.logging.Logger
import java.util.concurrent.TimeUnit

/**
 * Implementation of [Pulse].
 * @param server the [Server]
 * @param node the [Node]
 * @constructor Constructs a new pulse
 */
class PulseImpl(private val server: Server, private val node: Node) : Pulse {

    companion object {
        const val MARGIN = 500
    }

    override var lastHeartbeat: Long = System.currentTimeMillis()
    private val log = Logger.getLogger()

    init {
        waitForHeartbeat()
    }

    private fun waitForHeartbeat() {
        val future = server.eventWaiter.waitFor(WebSocketMessageEvent::class.java, {
            it.session == node.session
        }, (server.config.get<Long>(Config.SOCKET_HEARTBEAT) + MARGIN), TimeUnit.SECONDS)

        future.exceptionally {
            @Suppress("SpellCheckingInspection")
            log.warn("[WS] Disconnecting node ${node.session.id} for not sending heartbeat")
            node.session.disconnect()
            return@exceptionally null
        }.thenAccept {
            lastHeartbeat = System.currentTimeMillis()
            node.send(Payload.of(HeartBeatAckPacket(), HeartBeatAckPacket.IDENTIFIER))
            waitForHeartbeat()
        }
    }
}