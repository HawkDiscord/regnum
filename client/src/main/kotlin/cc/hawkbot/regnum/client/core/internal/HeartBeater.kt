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

import cc.hawkbot.regnum.client.events.websocket.WebSocketMessageEvent
import cc.hawkbot.regnum.entites.Payload
import cc.hawkbot.regnum.entites.packets.HeartBeatAckPacket
import cc.hawkbot.regnum.entites.packets.HeartBeatPacket
import cc.hawkbot.regnum.util.logging.Logger
import net.dv8tion.jda.api.hooks.SubscribeEvent

/**
 * Task for sending heartbeat
 */
class HeartBeater {

    private val log = Logger.getLogger()

    @SubscribeEvent
    @Suppress("unused")
    private fun onMessage(event: WebSocketMessageEvent) {
        val payload = Payload.fromJson(event.message)
        if (payload.type == HeartBeatPacket.IDENTIFIER) {
            log.info("[WS] Sending heartbeat")
            event.websocket.send(Payload.of(HeartBeatAckPacket(), HeartBeatAckPacket.IDENTIFIER))
        }
    }
}