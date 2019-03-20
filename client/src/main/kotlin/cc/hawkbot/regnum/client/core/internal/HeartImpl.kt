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
import cc.hawkbot.regnum.client.events.websocket.WebSocketMessageEvent
import cc.hawkbot.regnum.entites.Payload
import cc.hawkbot.regnum.entites.packets.HeartBeatAckPacket
import cc.hawkbot.regnum.entites.packets.HeartBeatPacket
import cc.hawkbot.regnum.entites.packets.HelloPacket
import cc.hawkbot.regnum.util.DefaultThreadFactory
import cc.hawkbot.regnum.util.logging.Logger
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Implementation of [Heart].
 * @property regnum the regnum instance
 * @param hello the [HelloPacket]
 */
class HeartImpl(
        private val regnum: Regnum,
        hello: HelloPacket
) : Heart {

    companion object {
        const val MARGIN = 500.toLong()
    }

    private val log = Logger.getLogger()
    private val scheduler = Executors.newSingleThreadScheduledExecutor(DefaultThreadFactory("HeartBeat"))
    private val future: ScheduledFuture<*>

    override var lastHeartbeat: Long = -1

    override var ping: Int = -1

    init {
        future = scheduler.scheduleAtFixedRate(this::beat, 0, hello.heartbeatInterval.toLong(), TimeUnit.SECONDS)
    }

    private fun beat() {
        log.info("[WS] Sending heartbeat")
        // Send heartbeat
        val start = System.currentTimeMillis()
        regnum.websocket.send(Payload.of(HeartBeatPacket(), HeartBeatPacket.IDENTIFIER))
        val event = regnum.eventWaiter.waitFor(WebSocketMessageEvent::class.java, { it.payload().type == HeartBeatAckPacket.IDENTIFIER }, MARGIN, TimeUnit.SECONDS)
        event.exceptionally {
            if (it is TimeoutException) {
                scheduler.shutdownNow()
                log.error("[WS] Closing websocket connection! Didn't received HEARTBEAT in time")
                regnum.websocket.close()
            }
            return@exceptionally null
        }
        event.thenRun {
            val end = System.currentTimeMillis()
            ping = ((end - start) / 1000L).toInt()
        }
    }
}