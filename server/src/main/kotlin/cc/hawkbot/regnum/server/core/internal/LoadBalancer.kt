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

package cc.hawkbot.regnum.server.core.internal

import cc.hawkbot.regnum.entites.Payload
import cc.hawkbot.regnum.entites.json.JsonObject
import cc.hawkbot.regnum.entites.packets.discord.StartPacket
import cc.hawkbot.regnum.server.plugin.Server
import cc.hawkbot.regnum.server.plugin.io.config.Config
import cc.hawkbot.regnum.util.logging.Logger
import io.javalin.websocket.WsSession
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LoadBalancer(private val server: Server) {

    private val log = Logger.getLogger()
    private val optimalShards: Int
    private var shardIds: IntRange
    private val ws = server.websocket
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private val counts = mutableMapOf<WsSession, Array<Int>>()

    init {
        optimalShards = calculateShardCount()
        shardIds = calculateShardIds(optimalShards)
    }

    private fun calculateShardCount(): Int {
        val request = Request.Builder()
                .addHeader("Authorization", server.config.getString(Config.DISCORD_TOKEN))
                .url("https://discordapp.com/api/gateway/bot")
                .build()
        val body = OkHttpClient().newCall(request).execute().body() ?: return 1
        return JsonObject(body.string()).getInt("shards")
    }

    private fun calculateShardIds(to: Int): IntRange {
        return 0 until to
    }

    fun handleConnect() {
        if (ws.nodes.size == 1) {
            log.info("[DiscordBalancer] First node connected waiting 30 seconds for other nodes to connect before balancing")
            scheduler.schedule(this::balance, 5, TimeUnit.SECONDS)
        }
    }

    private fun balance() {
        if (ws.nodes.isEmpty()) {
            log.warn("[DiscordBalancer] No nodes connected. Balancing aborted!")
            return
        }
        val shardsPerNode = Math.floor((optimalShards / ws.nodes.size).toDouble()).toInt()
        this.shardIds = calculateShardIds(optimalShards)
        val shardIds = this.shardIds.iterator()
        startNodes(shardsPerNode, shardIds)
    }

    fun rebalance(node: WsSession) {
        if (ws.nodes.isEmpty()) {
            log.warn("[DiscordBalancer] No nodes connected. Balancing aborted!")
            return
        }
        val missingShards = counts[node]!!
        log.info("[DiscordBalancer] Trying to rebalance ${missingShards.size} because ${node.id} disconnected")
        val shardsPerNode = Math.floor((missingShards.size / ws.nodes.size).toDouble()).toInt()
        startNodes(shardsPerNode, missingShards.iterator())
    }

    private fun startNodes(shardsPerNode: Int, shardIds: Iterator<Int>) {
        val nodes = ws.nodes.iterator()
        log.info("[DiscordBalancer] Balancing Discord shards on ${ws.nodes.size} nodes with $shardsPerNode shards/node")
        @Suppress("JoinDeclarationAndAssignment")
        while (nodes.hasNext()) {
            val node = nodes.next()
            val token = server.config.getString(Config.DISCORD_TOKEN)
            val shards: Array<Int>
            val shardsTotal = optimalShards
            val shardsList = mutableListOf<Int>()
            if (nodes.hasNext()) {
                for (i in 0 until shardsPerNode) {
                    val id = shardIds.next()
                    shardsList.add(id)
                }
            } else {
                while (shardIds.hasNext()) {
                    shardsList.add(shardIds.next())
                }
            }
            shards = shardsList.toTypedArray()
            counts[node] = shards
            ws.send(node, Payload.of(StartPacket(token, shards, shardsTotal), StartPacket.IDENTIFIER))
        }
    }
}