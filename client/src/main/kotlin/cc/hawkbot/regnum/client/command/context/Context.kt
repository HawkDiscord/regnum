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

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.command.ICommand
import cc.hawkbot.regnum.client.entities.RegnumGuild
import cc.hawkbot.regnum.client.entities.RegnumUser
import cc.hawkbot.regnum.client.util.FormatUtil
import cc.hawkbot.regnum.client.util.SafeMessage
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.requests.restaction.MessageAction

/**
 * Represents the context in which a command got executed.
 */
@Suppress("unused")
interface Context {

    /**
     * The event that invoked the command.
     */
    val event: GuildMessageReceivedEvent

    /**
     * The command.
     */
    val command: ICommand

    /**
     * The arguments the executor has provided.
     */
    val args: Arguments

    /**
     * The mentions inside the message.
     */
    val mentions: Mentions

    /**
     * The Regnum instance.
     */
    val regnum: Regnum

    /**
     * The message which invoked the command.
     */
    val message: Message
        get() = event.message

    /**
     * The [author] of the command as a [Member].
     */
    val member: Member
        get() = event.member

    /**
     * The author of the [message] which invoked the command.
     */
    val author: User
        get() = event.author

    /**
     * The [Guild] on which the command got executed.
     */
    val guild: Guild
        get() = event.guild

    /**
     * The [Member] of the bot on the [guild] where the command got executed
     * @see Guild.getSelfMember
     */
    val me: Member
        get() = guild.selfMember

    /**
     * The content of the [message].
     */
    val content: String
        get() = message.contentRaw

    /**
     * The [TextChannel] in which the [message] got sent.
     */
    val channel: TextChannel
        get() = event.channel

    /**
     * The [JDA] instance of the shard which received the command.
     */
    val jda: JDA
        get() = event.jda

    /**
     * Returns the (message)[Message].
     * @return the message
     */
    @Deprecated("We're moving fluent getters to Kotlin fields", ReplaceWith("message"))
    fun message(): Message {
        return message
    }

    /**
     * Returns the author as a [Member].
     * @return the author as a [Member]
     */
    @Deprecated("We're moving fluent getters to Kotlin fields", ReplaceWith("member"))
    fun member(): Member {
        return event.member
    }

    /**
     * Returns the (author)[User].
     * @return the (author)[User]
     */
    @Deprecated("We're moving fluent getters to Kotlin fields", ReplaceWith("author"))
    fun author(): User {
        return event.author
    }

    /**
     * Returns the [Member] of the bot.
     * @return the [Member] of the bot
     */
    @Deprecated("We're moving fluent getters to Kotlin fields", ReplaceWith("me"))
    fun me(): Member {
        return event.guild.selfMember
    }

    /**
     * Returns the [Guild] the command got executed on.
     * @return the [Guild] the command got executed on
     */
    @Deprecated("We're moving fluent getters to Kotlin fields", ReplaceWith("guild"))
    fun guild(): Guild {
        return event.guild
    }

    /**
     * Returns the content of the message.
     * @return the content of the message
     */
    @Deprecated("We're moving fluent getters to Kotlin fields", ReplaceWith("content"))
    fun content(): String {
        return event.message.contentRaw
    }

    /**
     * Returns the channel in which the command got executed.
     * @return the channel in which the command got executed
     */
    @Deprecated("We're moving fluent getters to Kotlin fields", ReplaceWith("channel"))
    fun channel(): TextChannel {
        return event.channel
    }

    /**
     * Returns the JDA instance.
     * @return the JDA instance
     */
    @Deprecated("We're moving fluent getters to Kotlin fields", ReplaceWith("jda"))
    fun jda(): JDA {
        return event.jda
    }

    /**
     * Sends a message using [SafeMessage].
     * @param message the message
     * @see SafeMessage.sendMessage
     * @return the resulting [MessageAction]
     */
    fun sendMessage(message: Message): MessageAction {
        return SafeMessage.sendMessage(message, channel)
    }

    /**
     * Sends a message using [SafeMessage].
     * @param message the message
     * @see SafeMessage.sendMessage
     * @return the resulting [MessageAction]
     */
    fun sendMessage(message: MessageBuilder): MessageAction {
        return SafeMessage.sendMessage(message, channel)
    }

    /**
     * Sends a message using [SafeMessage].
     * @param message the message
     * @see SafeMessage.sendMessage
     * @return the resulting [MessageAction]
     */
    fun sendMessage(message: String): MessageAction {
        return SafeMessage.sendMessage(message, channel)
    }

    /**
     * Sends a embed using [SafeMessage].
     * @param embed the embed
     * @see SafeMessage.sendMessage
     * @return the resulting [MessageAction]
     */
    fun sendMessage(embed: MessageEmbed): MessageAction {
        return SafeMessage.sendMessage(embed, channel)
    }

    /**
     * Sends a embed using [SafeMessage].
     * @param embed the embed
     * @see SafeMessage.sendMessage
     * @return the resulting [MessageAction]
     */
    fun sendMessage(embed: EmbedBuilder): MessageAction {
        return SafeMessage.sendMessage(embed, channel)
    }

    /**
     * Sends an auto generated help message
     * @see cc.hawkbot.regnum.client.util.FormatUtil.formatCommand
     * @return the message action
     */
    fun sendHelp(): MessageAction {
        return sendMessage(FormatUtil.formatCommand(command, regnumGuild(), regnum))
    }

    /**
     * Gets the translated string for the specified [key].
     * @param key the key
     * @return the string
     */
    fun translate(key: String): String {
        return regnum.languageManager.getLanguageByUser(author).translate(key)
    }

    /**
     * Returns the [RegnumGuild] which belongs to the [guild].
     * @return the regnum guild
     */
    fun regnumGuild(): RegnumGuild {
        return regnum.guild(guild)
    }

    /**
     * Returns the [RegnumUser] which belongs to the [author].
     * @return the regnum user
     */
    fun regnumUser(): RegnumUser {
        return regnum.user(author)
    }

}