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

@file:Suppress("MemberVisibilityCanBePrivate")

package cc.hawkbot.regnum.client.events.discord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent

/**
 * Extension of [GuildMessageUpdateEvent] which allows you to get the content of the message before it got edited.
 * @see GuildMessageUpdateEvent
 * @see MessageCacheEvent
 */
class GuildMessageUpdateEvent(api: JDA, responseNumber: Long, message: Message, override val oldContentRaw: String?) : GuildMessageUpdateEvent(api, responseNumber, message), MessageCacheEvent

/**
 * Extension of [GuildMessageDeleteEvent] which allows you to get the content of the message before it got deleted.
 * @see GuildMessageDeleteEvent
 * @see MessageCacheEvent
 */
class GuildMessageDeleteEvent(api: JDA, responseNumber: Long, messageId: Long, channel: TextChannel, override val oldContentRaw: String?) : GuildMessageDeleteEvent(api, responseNumber, messageId, channel), MessageCacheEvent

/**
 * Generic event that implements message cache support.
 * @property oldContentRaw the content of the message before it got edited. Can be null if it isn't cached
 */
private interface MessageCacheEvent {

    //TODO: Add MarkdownSanitizer support

    val oldContentRaw: String?

}