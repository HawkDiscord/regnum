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

package cc.hawkbot.regnum.client

import cc.hawkbot.regnum.client.command.CommandParser
import cc.hawkbot.regnum.client.command.permission.PermissionManager
import cc.hawkbot.regnum.client.command.translation.LanguageManager
import cc.hawkbot.regnum.client.core.Websocket
import cc.hawkbot.regnum.client.core.discord.Discord
import cc.hawkbot.regnum.client.entities.RegnumGuild
import cc.hawkbot.regnum.client.entities.RegnumUser
import cc.hawkbot.regnum.client.entities.cache.CassandraCache
import cc.hawkbot.regnum.client.util.MessageCache
import cc.hawkbot.regnum.io.database.CassandraSource
import cc.hawkbot.regnum.waiter.impl.EventWaiter
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.hooks.IEventManager

/**
 * Main class of Regnum client instance.
 */
@Suppress("unused")
interface Regnum {

    /**
     * The websocket instance.
     */
    val websocket: Websocket

    /**
     * The discord instance.
     */
    val discord: Discord

    /**
     * The event manager.
     */
    val eventManager: IEventManager

    /**
     * The token used for authentication.
     */
    val token: String

    /**
     * The command parser.
     */
    val commandParser: CommandParser

    /**
     * The language manager.
     */
    val languageManager: LanguageManager

    /**
     * The cassandra source.
     */
    val cassandra: CassandraSource

    /**
     * Ids of users with owner permissions.
     */
    val owners: List<Long>


    /**
     * The guild cache.
     */
    val guildCache: CassandraCache<RegnumGuild>

    /**
     * The user cache.
     */
    val userCache: CassandraCache<RegnumUser>

    /**
     * A message cache
     */
    val messageCache: MessageCache

    /**
     * The event waiter.
     */
    val eventWaiter: EventWaiter

    /**
     * The permission manager.
     */
    val permissionManager: PermissionManager

    /**
     * A list of all disabled features.
     */
    val disabledFeatures: List<Feature>

    /**
     * Returns a Regnum guild from the cache.
     * @param id the id of the guild
     * @return a Regnum guild from the cache
     */
    fun guild(id: Long): RegnumGuild {
        return guildCache[id]
    }

    /**
     * Returns a Regnum guild from the cache.
     * @param id the id of the guild
     * @return a Regnum guild from the cache
     */
    fun guild(id: String): RegnumGuild {
        return guildCache[id]
    }

    /**
     * Returns a Regnum guild from the cache.
     * @param guild the guild
     * @return a Regnum guild from the cache
     */
    fun guild(guild: Guild): RegnumGuild {
        return guildCache[guild]
    }

    /**
     * Returns the Regnum user grom the cache
     * @param id the id of the user
     * @return the user
     */
    fun user(id: Long): RegnumUser {
        return userCache[id]
    }

    /**
     * Returns the Regnum user grom the cache
     * @param id the id of the user
     * @return the user
     */
    fun user(id: String): RegnumUser {
        return userCache[id]
    }

    /**
     * Returns the Regnum user grom the cache
     * @param user the user
     * @return the user
     */
    fun user(user: User): RegnumUser {
        return userCache[user]
    }

}