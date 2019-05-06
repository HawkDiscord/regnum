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

package cc.hawkbot.regnum.client.core.internal

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.core.Heart
import cc.hawkbot.regnum.client.core.Websocket
import cc.hawkbot.regnum.client.events.Event
import cc.hawkbot.regnum.client.events.websocket.WebSocketCloseEvent
import cc.hawkbot.regnum.client.events.websocket.WebSocketConnectedEvent
import cc.hawkbot.regnum.client.events.websocket.WebSocketErrorEvent
import cc.hawkbot.regnum.client.events.websocket.WebSocketMessageEvent
import cc.hawkbot.regnum.entities.Payload
import cc.hawkbot.regnum.entities.packets.IdentifyPacket
import cc.hawkbot.regnum.net.PacketProcessor
import cc.hawkbot.regnum.util.logging.Logger
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.ConnectException
import java.net.URI

/**
 * Websocket client
 * @param location the host of the server
 * @property regnum the regnum instance
 * @constructor constructs a new websocket client
 */
class WebsocketImpl(
        location: URI,
        val regnum: Regnum
) : WebSocketClient(location), Websocket {

    private val log = Logger.getLogger()
    override val packetProcessor: PacketProcessor = PacketProcessor()
    override lateinit var heart: Heart

    init {
        packetProcessor.registerPackets(
                HelloHandler(regnum),
                StartHandler(regnum),
                AddHandler(regnum)
        )
    }

    /**
     * Websocket client
     * @param location the host of the server
     * @property regnum the regnum instance
     * @constructor constructs a new websocket client
     */
    constructor(location: String, regnum: Regnum) : this(URI(location), regnum)

    override fun onOpen(handshakedata: ServerHandshake) {
        log.info("[WS] Websocket connection opened with message {}: \"{}\"", handshakedata.httpStatus, handshakedata.httpStatusMessage)
        authorize()
        callEvent(WebSocketConnectedEvent(regnum, this, handshakedata))
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        log.error("[WS] Websocket connection got closed with code {} for reason {} remote={}", code, reason, remote)
        if (code == 1002) {
            throw ConnectException("Could not connect to websocket")
        }
        callEvent(WebSocketCloseEvent(regnum, this, code, reason, remote))
    }

    override fun onMessage(message: String) {
        log.info("[WS] Websocket message received: {}", message)
        packetProcessor.processMessage(message)
        callEventAsync(WebSocketMessageEvent(regnum, this, message))
    }

    override fun onError(ex: Exception) {
        log.error("[WS] An error with the WebSocket occurred", ex)
        callEventAsync(WebSocketErrorEvent(regnum, this, ex))
    }

    override fun start() = super.connect()

    private fun callEvent(event: Event) {
        regnum.eventManager.fireEvent(event)
    }

    private fun authorize() {
        log.info("[WS] Sending IDENTIFY")
        val identify = Payload.of(IdentifyPacket(regnum.token), IdentifyPacket.IDENTIFIER)
        send(identify)
    }

    override fun sendMessage(message: String) = this.send(message)

    private fun callEventAsync(event: Event) = regnum.eventManager.fireEvent(event)

    override fun close() = super.close()

    /**
     * Returns whether the [heart] is initialized or not.
     * @return whether the [heart] is initialized or not
     */
    fun isHeartInitialized() = this::heart.isInitialized

}
