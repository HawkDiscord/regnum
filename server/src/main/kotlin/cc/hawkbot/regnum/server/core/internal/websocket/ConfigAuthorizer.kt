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
import cc.hawkbot.regnum.entites.packets.IdentifyPacket
import cc.hawkbot.regnum.server.plugin.Server
import cc.hawkbot.regnum.server.plugin.core.AuthorizationHandler
import cc.hawkbot.regnum.server.plugin.events.websocket.WebSocketMessageEvent
import cc.hawkbot.regnum.server.plugin.io.config.Config
import cc.hawkbot.regnum.util.logging.Logger
import io.javalin.websocket.WsSession
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import java.lang.RuntimeException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit

class ConfigAuthorizer : AuthorizationHandler {

    private val log = Logger.getLogger()

    override fun authorize(server: Server, wsSession: WsSession): CompletionStage<WsSession> {
        log.info("[WS] NodeImpl {} connected. Waiting for IDENTIFY", wsSession.id)
        val config = server.config
        val connectFuture = CompletableFuture<WsSession>()
        val future = server.eventWaiter.waitFor(WebSocketMessageEvent::class.java, {
            it.session == wsSession
        }, config.getLong(Config.SOCKET_IDENTIFY), TimeUnit.SECONDS)
        future.exceptionally {
            log.warn("[Authorizer] ${wsSession.id} Got disconnected for not sending IDENTIFY in time")
            wsSession.disconnect()
            return@exceptionally null
        }
        future.thenAccept {
            val token = config.getString(Config.SOCKET_TOKEN)
            val payload = it.payload
            if (payload.type == IdentifyPacket.IDENTIFIER) {
                val identify = payload.packet as IdentifyPacket
                if (token == identify.token) {
                    connectFuture.complete(wsSession)
                } else {
                    log.info("[Authorizer] Disconnecting ${it.session.id} for sending wrong token")
                    wsSession.disconnect()
                    connectFuture.completeExceptionally(RuntimeException("invalid token"))
                }
            }
        }
        return connectFuture
    }
}