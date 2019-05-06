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
import cc.hawkbot.regnum.client.core.discord.impl.DiscordImpl
import cc.hawkbot.regnum.entities.packets.HelloPacket
import cc.hawkbot.regnum.entities.packets.Packet
import cc.hawkbot.regnum.entities.packets.discord.AddPacket
import cc.hawkbot.regnum.entities.packets.discord.StartPacket
import cc.hawkbot.regnum.net.PacketHandler
import kotlin.reflect.KClass

sealed class ClientPacketHandler<T : Packet>(protected val regnum: Regnum, packetIdentifier: String, packetClass: KClass<T>) : PacketHandler<T>(packetIdentifier, packetClass)

class HelloHandler(regnum: Regnum) : ClientPacketHandler<HelloPacket>(regnum, HelloPacket.IDENTIFIER, HelloPacket::class) {
    override fun processPacket(packet: HelloPacket) {
        (regnum as RegnumImpl).websocket.heart = HeartImpl(regnum, packet)
        regnum.metricsSender.start()
    }
}

class StartHandler(regnum: Regnum) : ClientPacketHandler<StartPacket>(regnum, StartPacket.IDENTIFIER, StartPacket::class) {
    override fun processPacket(packet: StartPacket) {
        val regnumImpl = regnum as RegnumImpl
        regnumImpl.discord = DiscordImpl(
                regnum,
                regnumImpl.shardManagerClass,
                packet.token,
                packet.shards,
                packet.shardsTotal
        )
    }
}

class AddHandler(regnum: Regnum) : ClientPacketHandler<AddPacket>(regnum, AddPacket.IDENTIFIER, AddPacket::class) {
    override fun processPacket(packet: AddPacket) = regnum.discord.addShards(packet.shards)
}
