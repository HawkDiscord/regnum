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
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent

/**
 * Listener which listen for MESSAGE events and updates the [cc.hawkbot.regnum.client.core.MessageCache].
 * @param regnum the Regnum instance
 * @constructor Constructs a new message watcher
 */
class MessageWatcher(
        regnum: Regnum
) {

    private val cache = regnum.messageCache
    private val eventManager = regnum.eventManager

    @SubscribeEvent
    @Suppress("unused")
    private fun handleMessageReceived(event: GuildMessageReceivedEvent) {
        cache[event.messageIdLong] = event.message.contentRaw
    }

    @SubscribeEvent
    @Suppress("unused")
    private fun handleMessageUpdate(event: GuildMessageUpdateEvent) {
        val oldContent = cache[event.messageIdLong]
        cache.update(event.messageIdLong, event.message.contentRaw)
        eventManager.handle(cc.hawkbot.regnum.client.events.discord.GuildMessageUpdateEvent(
                event.jda,
                event.responseNumber,
                event.message,
                oldContent
        ))
    }

    @SubscribeEvent
    @Suppress("unused")
    private fun handleMessageDelete(event: GuildMessageDeleteEvent) {
        val oldContent = cache[event.messageIdLong]
        cache.delete(event.messageIdLong)
        eventManager.handle(cc.hawkbot.regnum.client.events.discord.GuildMessageDeleteEvent(
                event.jda,
                event.responseNumber,
                event.messageIdLong,
                event.channel,
                oldContent
        ))
    }


}