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

import cc.hawkbot.regnum.entites.Payload
import cc.hawkbot.regnum.entites.packets.HeartBeatAckPacket
import cc.hawkbot.regnum.entites.packets.HeartBeatPacket
import cc.hawkbot.regnum.entites.packets.HelloPacket
import cc.hawkbot.regnum.entites.packets.IdentifyPacket
import cc.hawkbot.regnum.server.core.Server
import cc.hawkbot.regnum.server.core.Websocket
import cc.hawkbot.regnum.server.io.config.Config
import cc.hawkbot.regnum.util.logging.Logger
import io.javalin.websocket.WsHandler
import io.javalin.websocket.WsSession
import java.io.EOFException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class WebsocketImpl(ws: WsHandler, config: Config, server: Server) : Websocket {

    private val log = Logger.getLogger()
    private val token = config.getString(Config.SOCKET_TOKEN)
    private val heartbeat = config.getInt(Config.SOCKET_HEARTBEAT)
    private val identify = config.getInt(Config.SOCKET_IDENTIFY)
    override val nodes = mutableListOf<WsSession>()
    @Suppress("SpellCheckingInspection")
    private val authorizers = mutableMapOf<WsSession, ScheduledFuture<*>>()
    @Suppress("SpellCheckingInspection")
    private val heartbeaters = mutableMapOf<WsSession, ScheduledFuture<*>>()
    private val heartbeatSenders = mutableMapOf<WsSession, ScheduledFuture<*>>()
    private val executor = Executors.newScheduledThreadPool(5)
    private val loadBalancer: LoadBalancer

    init {
        (server as ServerImpl).websocket = this
        loadBalancer = LoadBalancer(server)
        ws.onConnect {
            handleConnect(it)
        }
        ws.onClose { session, statusCode, reason ->
            nodes.remove(session)
            println(session.id + "Disconnected")
            loadBalancer.rebalance(session)
        }
        ws.onMessage { session, msg -> handleMessage(session, msg) }
        ws.onError { session, throwable -> handleError(session, throwable) }
    }

    private fun handleConnect(it: WsSession) {
        log.info("[WS] Node {} connected. Waiting for IDENTIFY", it.id)
        executor.schedule(
                {
                    if (it !in nodes) {
                        log.info("[WS] Closing connection to {} because no IDENTIFY in time", it.id)
                        //it.close(403, "Identify timeout exceeded")
                        disconnectNode(it)
                    }
                },
                identify.toLong(),
                TimeUnit.SECONDS
        )
    }

    private fun handleMessage(session: WsSession, msg: String) {
        val payload = Payload.fromJson(msg)
        when (payload.type) {
            IdentifyPacket.IDENTIFIER -> {
                val identify = payload.packet as IdentifyPacket
                val token = identify.token
                if (token == this.token) {
                    log.info("[WS] Received valid IDENTIFY from {}", session.id)
                    // Stop authorizer
                    authorizers[session]?.cancel(true)
                    nodes.add(session)
                    send(session, Payload.of(HelloPacket(heartbeat), HelloPacket.IDENTIFIER))
                    startHeartbeater(session)

                    // Start loadbalancer
                    loadBalancer.handleConnect()
                } else {
                    log.info("[WS] Disconnecting {} because it send a wrong IDENTIFY", session.id)
                    disconnectNode(session)
                }
            }
            HeartBeatAckPacket.IDENTIFIER -> {
                val future = heartbeaters[session] ?: return
                future.cancel(true)
                log.info("[WS] Received HEARTBEAT_ACK from {}", session.id)
            }
        }
    }

    private fun handleError(session: WsSession, throwable: Throwable?) {
        log.error("[WS] An error occurred on session $session", throwable)
    }

    override fun send(session: WsSession, message: String) {
        session.send(message)
    }

    private fun startHeartbeater(session: WsSession) {
        heartbeatSenders[session] = executor.scheduleAtFixedRate({
            log.info("[WS] Sending HEARTBEAT to {}", session.id)
            send(session, Payload.of(HeartBeatPacket(), HeartBeatPacket.IDENTIFIER))
            heartbeaters[session] = executor.schedule({
                log.info("[WS] Disconnecting {} for not sending heartbeat in time", session.id)
                heartbeatSenders[session]?.cancel(true)
                session.disconnect()
            }, 5, TimeUnit.SECONDS)
        }, heartbeat.toLong(), heartbeat.toLong(), TimeUnit.SECONDS)
    }

    private fun disconnectNode(session: WsSession) {
        try {
            session.disconnect()
        } catch (e: EOFException) {
            // ignored
        }
    }
}