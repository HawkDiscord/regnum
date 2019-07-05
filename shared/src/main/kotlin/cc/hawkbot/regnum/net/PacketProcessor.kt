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

package cc.hawkbot.regnum.net

import cc.hawkbot.regnum.entities.Payload
import cc.hawkbot.regnum.entities.json.Json
import cc.hawkbot.regnum.entities.packets.Packet
import cc.hawkbot.regnum.util.logging.Logger
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import java.lang.Exception

class PacketProcessor {

    private val log = Logger.getLogger()
    private val packets = mutableMapOf<String, PacketHandler<*>>()

    fun registerPackets(vararg handlers: PacketHandler<*>) {
        handlers.forEach {
            packets[it.packetIdentifier] = it
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun processMessage(message: String) {
        val payload = Payload.fromJson(message)
        val handler = packets[payload.type] ?: return
        processHandler(handler, payload)
    }

    private fun <T : Packet> processHandler(handler: PacketHandler<T>, payload: Payload) {
        val packet: T = try {
            Json.fromJson(handler.packetClass.java, payload.packet)
        } catch (e: JsonParseException) {
            log.warn("[PacketProcessor] Packet could not be processed due to a parsing error", e)
            return
        } catch (e: JsonMappingException) {
            log.warn("[PacketProcessor] Packet could not be processed due to a mapping error", e)
            return
        } ?: return
        handler.processPacket(packet)
    }
}