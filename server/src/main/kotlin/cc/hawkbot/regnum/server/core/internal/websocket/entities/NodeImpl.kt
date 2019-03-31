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

package cc.hawkbot.regnum.server.core.internal.websocket.entities

import cc.hawkbot.regnum.entites.Payload
import cc.hawkbot.regnum.entites.packets.discord.AddPacket
import cc.hawkbot.regnum.entites.packets.discord.StartPacket
import cc.hawkbot.regnum.server.plugin.Server
import cc.hawkbot.regnum.server.plugin.Websocket
import cc.hawkbot.regnum.server.plugin.entities.Node
import cc.hawkbot.regnum.server.plugin.entities.Pulse
import io.javalin.websocket.WsSession

/**
 * Implementation of [Node].
 * @param server the [WsSession]
 * @param websocket the [Websocket]
 * @param server the server
 */
class NodeImpl(override val session: WsSession, override val websocket: Websocket, server: Server) : Node {

    // No shards for now
    override var shards: Array<Int> = arrayOf()
    override val pulse: Pulse = PulseImpl(server, this)
    private val loadBalancer = server.loadBalancer

    override fun startShards(shards: Array<Int>) {
        this.shards = shards
        send(Payload.of(StartPacket(loadBalancer.token, shards, loadBalancer.optimalShards), StartPacket.IDENTIFIER))
    }

    override fun addShards(shards: Array<Int>) {
        this.shards += shards
        send(Payload.of(AddPacket(shards), AddPacket.IDENTIFIER))
    }

}