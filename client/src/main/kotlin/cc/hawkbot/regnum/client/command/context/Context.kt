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
     * Returns the (message)[Message].
     * @return the message
     */
    fun message(): Message {
        return event.message
    }

    /**
     * Returns the author as a [Member].
     * @return the author as a [Member]
     */
    fun member(): Member {
        return event.member
    }

    /**
     * Returns the (author)[User].
     * @return the (author)[User]
     */
    fun author(): User {
        return event.author
    }

    /**
     * Returns the [Member] of the bot.
     * @return the [Member] of the bot
     */
    fun me(): Member {
        return event.guild.selfMember
    }

    /**
     * Returns the [Guild] the command got executed on.
     * @return the [Guild] the command got executed on
     */
    fun guild(): Guild {
        return event.guild
    }

    /**
     * Returns the content of the message.
     * @return the content of the message
     */
    fun content(): String {
        return event.message.contentRaw
    }

    /**
     * Returns the channel in which the command got executed.
     * @return the channel in which the command got executed
     */
    fun channel(): TextChannel {
        return event.channel
    }

    /**
     * Returns the JDA instance.
     * @return the JDA instance
     */
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
        return SafeMessage.sendMessage(message, channel())
    }

    /**
     * Sends a message using [SafeMessage].
     * @param message the message
     * @see SafeMessage.sendMessage
     * @return the resulting [MessageAction]
     */
    fun sendMessage(message: MessageBuilder): MessageAction {
        return SafeMessage.sendMessage(message, channel())
    }

    /**
     * Sends a message using [SafeMessage].
     * @param message the message
     * @see SafeMessage.sendMessage
     * @return the resulting [MessageAction]
     */
    fun sendMessage(message: String): MessageAction {
        return SafeMessage.sendMessage(message, channel())
    }

    /**
     * Sends a embed using [SafeMessage].
     * @param embed the embed
     * @see SafeMessage.sendMessage
     * @return the resulting [MessageAction]
     */
    fun sendMessage(embed: MessageEmbed): MessageAction {
        return SafeMessage.sendMessage(embed, channel())
    }

    /**
     * Sends a embed using [SafeMessage].
     * @param embed the embed
     * @see SafeMessage.sendMessage
     * @return the resulting [MessageAction]
     */
    fun sendMessage(embed: EmbedBuilder): MessageAction {
        return SafeMessage.sendMessage(embed, channel())
    }

    /**
     * Sends an auto generated help message
     * @see cc.hawkbot.regnum.client.util.FormatUtil.formatCommand
     * @return the message action
     */
    fun sendHelp(): MessageAction {
        return sendMessage(FormatUtil.formatCommand(command, "", regnum))
    }

    //TODO: Translation method
    //TODO: Database entity getters
}