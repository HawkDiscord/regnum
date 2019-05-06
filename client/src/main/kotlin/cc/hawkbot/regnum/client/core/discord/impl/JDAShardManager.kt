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

package cc.hawkbot.regnum.client.core.discord.impl

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.core.discord.GameAnimator
import cc.hawkbot.regnum.client.core.discord.ShardManager
import cc.hawkbot.regnum.client.core.discord.ShardWatcher
import cc.hawkbot.regnum.client.core.internal.RegnumImpl
import cc.hawkbot.regnum.client.event.EventManager
import cc.hawkbot.regnum.client.event.EventSubscriber
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.IEventManager
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder

class JDAShardManager : ShardManager {

    lateinit var jda: net.dv8tion.jda.api.sharding.ShardManager

    override val averageRestPing: Long
        get() = -1
    override val averageGatewayPing: Long
        get() = jda.averageGatewayPing.toLong()
    override val shardsTotal: Any
        get() = jda.shardsTotal
    override val guildsSize: Int
        get() = jda.guilds.size
    override val userSize: Int
        get() = jda.users.size
    override lateinit var shardWatcher: ShardWatcher

    override fun shutdown()  = jda.shutdown()

    override fun applyGame(game: GameAnimator.Game) {
        jda.setPresence(
                OnlineStatus.fromKey(game.status),
                Activity.of(Activity.ActivityType.fromKey(game.type), game.content)
        )
    }

    override fun addShard(id: Int) = jda.start(id)

    override fun start(token: String, shards: Array<Int>, shardsTotal: Int, regnum: RegnumImpl) {
        shardWatcher = ShardWatcher(regnum, shards.size)
        val builder = DefaultShardManagerBuilder()
                .setToken(token)
                .setShardsTotal(shardsTotal)
                .setShards(shards.toList())
                .setEventManagerProvider { JDAEventManager(regnum.eventManager) }
                .addEventListeners(JDAShardWatcher(regnum))
                .setActivity(Activity.playing("Starting ..."))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)

        jda = builder.build()
    }

    @Suppress("unused")
    private inner class JDAShardWatcher(private val regnum: Regnum) {

        @Suppress("UNUSED_PARAMETER")
        @EventSubscriber
        private fun done(event: cc.hawkbot.regnum.client.events.discord.ReadyEvent) {
            regnum.eventManager.unregister(this)
        }

        @EventSubscriber
        private fun onReady(event: ReadyEvent) {
            shardWatcher.shardReady(event.guildAvailableCount, event.guildUnavailableCount)
        }

        @EventSubscriber
        @Suppress("UNUSED_PARAMETER")
        private fun onDisconnect(event: DisconnectEvent) {
            shardWatcher.shardDisconnected(guildsSize, 0)

        }
    }

    private class JDAEventManager(private val eventManager: EventManager): IEventManager {
        override fun handle(event: GenericEvent) = eventManager.fireEvent(event)

        override fun register(listener: Any) = eventManager.register(listener)

        override fun getRegisteredListeners() = eventManager.registeredListeners

        override fun unregister(listener: Any) = eventManager.unregister(listener)

    }
}