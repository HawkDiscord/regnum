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
import cc.hawkbot.regnum.client.core.discord.Discord
import cc.hawkbot.regnum.client.core.discord.GameAnimator
import cc.hawkbot.regnum.client.core.discord.ShardWatcher
import cc.hawkbot.regnum.client.core.internal.RegnumImpl
import cc.hawkbot.regnum.util.logging.Logger
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.SubscribeEvent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager

/**
 * Implementation of [Discord].
 * @property regnum the Regnum instance
 * @param token the Discord bot token
 * @param shards the ids of shards this instance should start
 * @param shardsTotal the total count of shards
 *
 * @constructor Constructs a new Discord client
 */
@Suppress("UNSAFE_CAST", "unused")
class DiscordImpl(
        val regnum: Regnum,
        token: String,
        shards: Array<Int>,
        shardsTotal: Int
) : Discord {

    private val log = Logger.getLogger()
    override val shardManager: ShardManager
    override val gameAnimator: GameAnimator
    private var availableGuilds: Int = 0
    private var unavailableGuilds: Int = 0

    init {
        (regnum as RegnumImpl).discord = this
        log.info("[Discord] Starting shards ${shards.joinToString()} total $shardsTotal")
        val builder = DefaultShardManagerBuilder()
                .setToken(token)
                .setShardsTotal(shardsTotal)
                .setShards(shards.toList())
                .setEventManagerProvider { regnum.eventManager }
                .addEventListeners(this, ShardWatcher(regnum, shards.size))
                .setActivity(Activity.playing("Starting ..."))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)

        shardManager = builder.build()
        gameAnimator = GameAnimator(regnum)
    }

    /**
     * @see Discord.addShards
     */
    override fun addShards(shards: Array<out Int>) {
        log.info("[Discord] Adding $shards because a other node needs to be replaced")
        shards.forEach {
            shardManager.start(it)
        }
    }

    @SubscribeEvent
    @Suppress("unused")
    private fun whenReady(event: cc.hawkbot.regnum.client.events.discord.ReadyEvent) {
        log.info("[Discord] Connected to discord with ${event.guildAvailableCount}/${event.guildUnavailableCount} available/unavailable guilds.")
        gameAnimator.start()
    }
}
