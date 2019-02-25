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

package cc.hawkbot.regnum.client.core.discord

import cc.hawkbot.regnum.client.Regnum
import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent

class ShardWatcher(private val regnum: Regnum, private val shardsTotal: Int) {

    private var shardsConnected = 0
    private var availableGuilds = 0
    private var unavailableGuilds = 0

    @Suppress("unused")
    @SubscribeEvent
    fun shardReady(event: ReadyEvent) {
        shardsConnected++
        availableGuilds += event.guildAvailableCount
        unavailableGuilds += event.guildUnavailableCount

        if (shardsConnected == shardsTotal) {
            regnum.eventManager.handle(cc.hawkbot.regnum.client.events.discord.ReadyEvent(regnum.discord.shardManager,
                    availableGuilds, unavailableGuilds))
            regnum.eventManager.unregister(this)
        }
    }

    @Suppress("unused")
    @SubscribeEvent
    fun shardDisconnected(@Suppress("UNUSED_PARAMETER") event: DisconnectEvent) {
        shardsConnected--
    }
}