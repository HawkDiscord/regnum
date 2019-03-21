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

import cc.hawkbot.regnum.entites.packets.MetricsPacket
import cc.hawkbot.regnum.server.plugin.Websocket
import cc.hawkbot.regnum.server.plugin.events.websocket.WebSocketMessageEvent
import cc.hawkbot.regnum.util.logging.Logger
import net.dv8tion.jda.api.hooks.SubscribeEvent

class MetricsWatcher {

    private val log = Logger.getLogger()

    @SubscribeEvent
    @Suppress("unused")
    private fun onMetrics(event: WebSocketMessageEvent) {
        val payload = event.payload
        if (payload.type == MetricsPacket.IDENTIFIER) {
            log.info("[Metrics] Received METRICS from ${event.node.id}")
            val metrics = payload.packet as MetricsPacket
            (event.websocket as WebsocketImpl).metrics[event.node] = metrics
            val average = calculateMetrics(event.websocket)
            event.server.averageMetrics = average
        }
    }

    private fun calculateMetrics(websocket: Websocket): MetricsPacket {
        var discordRestPing = 0L
        var discordGatewayPing = 0L
        var usedMemory = 0L
        var availableMemory = 0L
        var cpuUsage = 0L
        var cpus = 0
        var guilds = 0L
        var users = 0L

        websocket.nodes.map { it.metrics }.forEach {
            discordRestPing += it.discordRestPing
            discordGatewayPing += it.discordGatewayPing
            usedMemory += it.usedMemory
            availableMemory += it.availableMemory
            cpuUsage += it.cpuUsage
            cpus += it.cpus
            guilds += it.guilds
            users += it.users
        }

        // Average pings
        val total = websocket.nodes.size
        discordGatewayPing /= total
        discordRestPing /= total

        return MetricsPacket(discordRestPing, discordGatewayPing, usedMemory, availableMemory, cpuUsage, cpus, guilds, users)
    }
}