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

package cc.hawkbot.regnum.server.core.internal.websocket

import cc.hawkbot.regnum.entites.Payload
import cc.hawkbot.regnum.entites.packets.HeartBeatAckPacket
import cc.hawkbot.regnum.entites.packets.HeartBeatPacket
import cc.hawkbot.regnum.server.core.internal.ServerImpl
import cc.hawkbot.regnum.server.core.internal.websocket.entities.NodeImpl
import cc.hawkbot.regnum.server.plugin.Server
import cc.hawkbot.regnum.server.plugin.Websocket
import cc.hawkbot.regnum.server.plugin.entities.Node
import cc.hawkbot.regnum.server.plugin.events.websocket.*
import cc.hawkbot.regnum.server.plugin.io.config.Config
import cc.hawkbot.regnum.util.logging.Logger
import io.javalin.websocket.WsHandler
import io.javalin.websocket.WsSession
import java.io.EOFException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class WebsocketImpl(ws: WsHandler, config: Config, private val server: Server) : Websocket {

    private val log = Logger.getLogger()
    override val nodes = mutableListOf<Node>()
    private val loadBalancer: LoadBalancer
    private val authorizationHandler = server.authorizationHandler

    init {
        (server as ServerImpl).websocket = this
        loadBalancer = LoadBalancer(server)
        ws.onConnect {
            handleConnect(it)
        }
        ws.onClose { session, statusCode, reason ->
            callEvent(WebSocketCloseEvent(server, this, session, statusCode, reason))
            val node = nodes.firstOrNull { it.session == session } ?: return@onClose
                nodes.remove(node)
            println(session.id + "Disconnected")
            loadBalancer.rebalance(session)
        }
        ws.onMessage { session, msg -> handleMessage(session, msg) }
        ws.onError { session, throwable -> handleError(session, throwable) }
    }

    private fun handleConnect(it: WsSession) {
        callEvent(WebSocketConnectedEvent(server, this, it))
        authorizationHandler.authorize(server, it)
                .thenAccept {
                    log.info("[WS] ${it.id} connected!")
                    nodes.add(NodeImpl(it))
                }
                .exceptionally {
                    // IGNORE IT //
                    null
                }
    }

    private fun handleMessage(session: WsSession, msg: String) {
        log.info("[WS] Received message from node {} with content {}", session.id, msg)
        callEvent(WebSocketMessageEvent(server, this, session, msg))
    }

    private fun handleError(session: WsSession, throwable: Throwable?) {
        callEvent(WebSocketErrorEvent(server, this, session, throwable))
        log.error("[WS] An error occurred on session $session", throwable)
    }

    override fun send(session: WsSession, message: String) {
        session.send(message)
    }

    private fun callEvent(event: WebSocketEvent) {
        server.eventManager.handle(event)
    }
}