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

import cc.hawkbot.regnum.client.core.discord.impl.DiscordImpl
import cc.hawkbot.regnum.client.events.websocket.WebSocketMessageEvent
import cc.hawkbot.regnum.entites.Payload
import cc.hawkbot.regnum.entites.packets.HelloPacket
import cc.hawkbot.regnum.entites.packets.discord.AddPacket
import cc.hawkbot.regnum.entites.packets.discord.StartPacket
import net.dv8tion.jda.api.hooks.SubscribeEvent

/**
 * Listener for websocket packets
 * @property regnum the regnum implementation
 * @constructor Constructs a new packet handler
 */
class PacketHandler(val regnum: RegnumImpl) {

    @Suppress("unused")
    @SubscribeEvent
    private fun onMessage(event: WebSocketMessageEvent) {
        val payload = Payload.fromJson(event.message)
        when (payload.type) {
            HelloPacket.IDENTIFIER -> {
                val hello = payload.packet as HelloPacket
                regnum.websocket.heart = HeartImpl(regnum, hello)
            }
            StartPacket.IDENTIFIER -> {
                val start = payload.packet as StartPacket
                regnum.discord = DiscordImpl(
                        regnum,
                        start.token,
                        start.shards,
                        start.shardsTotal
                )
            }
            AddPacket.IDENTIFIER -> {
                val add = payload.packet as AddPacket
                regnum.discord.addShards(add.shards)
            }
        }
    }
}