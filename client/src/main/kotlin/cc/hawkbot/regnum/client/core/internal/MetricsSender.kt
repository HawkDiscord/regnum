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
import cc.hawkbot.regnum.entities.Payload
import cc.hawkbot.regnum.entities.packets.MetricsPacket
import java.io.Closeable
import java.lang.management.ManagementFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Metrics sender for node metrics.
 * @param regnum current regnum instance
 */
class MetricsSender(private val regnum: Regnum) : Closeable {

    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    /**
     * Starts metrics sender thread.
     */
    fun start() {
        scheduler.scheduleAtFixedRate(this::post, 0, 5, TimeUnit.MINUTES)
    }

    private fun post() {
        val restPing: Long
        val wsPing: Long
        val guilds: Long
        val users: Long
        if ((regnum as RegnumImpl).discordInitialized()) {
            val discord = regnum.discord.shardManager
            restPing = discord.shards[0].restPing.complete()
            wsPing = discord.averageGatewayPing.toLong()
            guilds = discord.guildCache.size()
            users = discord.userCache.size()
        } else {
            restPing = 0
            wsPing = 0
            guilds = 0
            users = 0
        }

        val memory = Runtime.getRuntime().maxMemory()
        val usedMemory = memory - Runtime.getRuntime().freeMemory()
        val bean = ManagementFactory.getOperatingSystemMXBean()
        val cpuUsage = bean.availableProcessors.toLong()
        val cpus = bean.availableProcessors
        regnum.websocket.send(Payload.of(MetricsPacket(restPing, wsPing, usedMemory, memory, cpuUsage, cpus, guilds, users), MetricsPacket.IDENTIFIER))
    }

    override fun close() {
        scheduler.shutdown()
    }
}