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

package cc.hawkbot.regnum.client.core

/**
 * Cache of messages to provide them on MESSAGE_UPDATE and MESSAGE_DELETE.
 * @see cc.hawkbot.regnum.client.core.discord.MessageWatcher
 */
interface MessageCache {

    /**
     * Deletes the message with the specified [messageId] from the cache.
     *      * This fires an [cc.hawkbot.regnum.client.events.discord.GuildMessageDeleteEvent]

     */
    fun delete(messageId: Long)

    /**
     * Adds the message with the specified [messageId] and [messageContent] to the cache.
     */
    operator fun set(messageId: Long, messageContent: String)

    /**
     * Updated the message with the specified [messageId] and [messageContent] to the cache.
     * This fires an [cc.hawkbot.regnum.client.events.discord.GuildMessageUpdateEvent]
     */
    fun update(messageId: Long, messageContent: String)

    /**
     * Reads the message with the specified [messageId] from cache.
     * @return the message
     */
    operator fun get(messageId: Long): String?
}