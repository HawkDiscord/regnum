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

package cc.hawkbot.regnum.client.command.context

import cc.hawkbot.regnum.client.util.Misc
import net.dv8tion.jda.api.entities.*
import java.util.*

/**
 * Represents all mentions in a commands message
 */
@Suppress("unused", "UNSAFE_CAST")
interface Mentions {

    /**
     * Whether the message mentioned everyone or not.
     */
    val everyone: Boolean

    /**
     * Returns whether there are mentions or not.
     * @return whether there are mentions or not
     */
    fun isEmpty(): Boolean {
        return list().isEmpty()
    }

    /**
     * Returns all mentions in a list.
     * @return all mentions in a list
     */
    fun list(): List<IMentionable>

    /**
     * Returns all mentions of a specific type in a list.
     * @param type the type of the mentions
     * @return all mentions of a specific type in a list
     */
    fun list(vararg type: Message.MentionType): List<IMentionable>

    /**
     * Returns all channel mentions.
     * @return all channel mentions
     */
    fun channels(): List<TextChannel> {
        return list(Message.MentionType.CHANNEL).map { it as TextChannel }
    }

    /**
     * Returns all role mentions.
     * @return all role mentions
     */
    fun roles(): List<Role> {
        return list(Message.MentionType.ROLE).map { it as Role }
    }

    /**
     * Returns all user mentions.
     * @return all user mentions
     */
    fun users(): List<User> {
        return list(Message.MentionType.USER).map { it as User }
    }

    /**
     * Returns all member mentions.
     * @return all member mentions
     */
    fun members(): List<Member>

    /**
     * Returns the first mention of a specific type.
     * @param type the type of the mention
     * @return an [Optional] containing the mention
     */
    fun first(type: Message.MentionType): Optional<IMentionable> {
        val list = list(type)
        val first = Misc.first(list)
        return Optional.ofNullable(first)
    }

    /**
     * Returns the first channel mention.
     * @return an [Optional] containing the channel
     */
    fun firstChannel(): Optional<TextChannel> {
        return first(Message.MentionType.CHANNEL).map { it as TextChannel }
    }

    /**
     * Returns the first role mention.
     * @return an [Optional] containing the role
     */
    fun firstRole(): Optional<Role> {
        return first(Message.MentionType.ROLE).map { it as Role }
    }

    /**
     * Returns the first user mention.
     * @return an [Optional] containing the user
     */
    fun firstUser(): Optional<User> {
        return first(Message.MentionType.USER).map { it as User }
    }

    /**
     * Returns the first member mention.
     * @return an [Optional] containing the member
     */
    fun firstMember(): Optional<Member> {
        return members().stream().findFirst()
    }
}