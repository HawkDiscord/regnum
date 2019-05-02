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

package cc.hawkbot.regnum.client.interaction

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.util.EmbedUtil
import cc.hawkbot.regnum.client.util.SafeMessage
import cc.hawkbot.regnum.client.util.TranslationUtil
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * Implementation of [InteractableMessage] which is able to handle yes/no questions.
 * @see ConfirmationMessageBuilder
 */
class ConfirmationMessage(
        regnum: Regnum,
        message: Message,
        users: List<User>,
        timeout: Long,
        timeUnit: TimeUnit,
        private val yesEmote: String,
        private val yesKeyword: String,
        private val noEmote: String,
        private val noKeyword: String,
        private val yes: Consumer<Context>,
        private val no: Consumer<Context>
) : ReactableMessage(regnum, message, users, timeout, timeUnit, listOf(Permission.MESSAGE_MANAGE, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION), true, false) {

    init {
        CompletableFuture.allOf(
                message.addReaction(yesEmote).submit(),
                message.addReaction(noEmote).submit()
        ).thenAccept { waitForInteraction() }
    }

    override fun handleReaction(event: GuildMessageReactionAddEvent) {
        val emote = event.reactionEmote.name
        val context = Context(event, event.channel.retrieveMessageById(event.messageId).complete())
        when (emote) {
            yesEmote -> finish(yes, context)
            noEmote -> finish(no, context)
            else -> retry()
        }
    }

    override fun onMessage(event: GuildMessageReceivedEvent) {
        val content = event.message.contentDisplay
        val context = Context(event)
        when (content) {
            yesKeyword -> finish(yes, context)
            noKeyword -> finish(no, context)
            else -> {
                SafeMessage.sendMessage(
                        EmbedUtil.error(
                                translate(
                                        "phrases.confirmation.invalid.title"
                                ),
                                translate(
                                        "phrases.confirmation.invalid.description"
                                )
                                        .format(yesKeyword, noKeyword)
                        ),
                        context.channel(),
                        5
                )
                retry()
            }
        }
    }

    private fun finish(consumer: Consumer<Context>, context: Context) {
        consumer.accept(context)
        finish()
    }


}