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

import cc.hawkbot.regnum.entites.json.JsonObject
import cc.hawkbot.regnum.server.plugin.Server
import cc.hawkbot.regnum.server.plugin.core.LoadBalancer
import cc.hawkbot.regnum.server.plugin.entities.Node
import cc.hawkbot.regnum.server.plugin.events.websocket.WebSocketCloseEvent
import cc.hawkbot.regnum.server.plugin.events.websocket.WebsocketAuthorizedEvent
import cc.hawkbot.regnum.server.plugin.io.config.Config
import cc.hawkbot.regnum.util.DefaultThreadFactory
import cc.hawkbot.regnum.util.logging.Logger
import net.dv8tion.jda.api.hooks.SubscribeEvent
import okhttp3.Request
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Implementation of [LoadBalancer].
 * @param server the server instance
 * @constructor Constructs a new LoadBalancer
 */
class LoadBalancerImpl(server: Server) : LoadBalancer {

    private val log = Logger.getLogger()

    override val optimalShards: Int
    @Suppress("JoinDeclarationAndAssignment")
    override val token: String
    private val ws = server.websocket
    private val scheduler = Executors.newSingleThreadScheduledExecutor(DefaultThreadFactory("LoadBalancer"))
    private var balanced = false
    private lateinit var waitFuture: ScheduledFuture<*>

    init {
        token = server.config.get(Config.DISCORD_NODE_TOKEN)
        optimalShards = calculateShardCount(server)
        log.info("[Balancer] Discovered initial optimal shard count of $optimalShards shards")
    }

    private fun calculateShardCount(server: Server): Int {
        val request = Request.Builder()
                .addHeader("Authorization", token)
                .url("https://discordapp.com/api/gateway/bot")
                .build()
        server.httpClient.newCall(request).execute().use {
            val body = it.body() ?: return -1
            return JsonObject(body.string()).getInt("shards")
        }
    }

    @SubscribeEvent
    override fun handleConnect(event: WebsocketAuthorizedEvent) {
        try {
            val node = event.node
            // Check if it's the first node
            if (ws.nodes.size == 1) {
                // Check if we only need one node
                if (optimalShards == 1) {
                    log.info("[Balancer] Starting shards immediately because enough nodes got connected ")
                    balance()
                    return
                }
                // Wait for other nodes to connect
                val timeout = event.server.config.getLong(Config.LOAD_BALANCE_TIMEOUT)
                log.info("[Balancer] First node connected waiting $timeout seconds for other nodes to connect!")
                waitFuture = scheduler.schedule(this::balance, timeout, TimeUnit.SECONDS)
            } else {
                // Check if shards are already balance
                if (balanced) {
                    node.disconnect()
                } else {
                    // Check if enough nodes are connected
                    if (ws.nodes.size == optimalShards) {
                        if (waitFuture.cancel(false)) {
                            balance()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SubscribeEvent
    override fun handleDisconnect(event: WebSocketCloseEvent) {
        balanced = false
        rebalance(event.node)
    }


    private fun balance() {
        // Check if nodes are connected
        if (ws.nodes.isEmpty()) {
            log.warn("[Balancer] No nodes connected! Aborting balancing!")
            return
        }

        // Calculate shard ids
        val shardsPerNode = Math.floor((optimalShards / ws.nodes.size).toDouble()).toInt()
        val shardIds = shardIds()
        // Start shards
        startShards(shardsPerNode, shardIds.toList().toTypedArray())
    }

    private fun rebalance(node: Node) {
        // Check if nodes are connected
        if (ws.nodes.isEmpty()) {
            log.warn("[Balancer] No nodes connected! Aborting balancing!")
            return
        }

        // Calculate offline shards
        val missingShards = node.shards
        log.warn("[Balancer] ${node.id} disconnected! Balancing $missingShards on other nodes")
        val shardsPerNode = Math.floor((missingShards.count() / ws.nodes.size).toDouble()).toInt()

        // Start offline shards
        startShards(shardsPerNode, missingShards)
    }

    private fun startShards(shardsPerNode: Int, shards: Array<Int>) {
        val nodes = ws.nodes.iterator()
        log.info("[DiscordBalancer] Balancing Discord shards on $shards nodes with $shardsPerNode shards/node")
        val shardIds = shards.iterator()
        while (nodes.hasNext()) {
            val shardsList = mutableListOf<Int>()
            val node = nodes.next()

            /**
             * Adds the next shard to the shards-list
             */
            fun addNextId() {
                shardsList.add(shardIds.next())
            }

            // Check if its last node
            if (nodes.hasNext()) {
                for (i in 0 until shardsPerNode) {
                    addNextId()
                }
            } else {
                // Add all missing shards to the last node
                while (shardIds.hasNext()) {
                    addNextId()
                }
            }
            node.startShards(shardsList.toTypedArray())
        }
        balanced = true
    }
}