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
import cc.hawkbot.regnum.client.util.SafeMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * Extension of [InteractableMessage] which allows you to also accept reaction based interactions.
 * @see ReactableMessageBuilder
 */
abstract class ReactableMessage(
        regnum: Regnum,
        message: Message,
        users: List<User>,
        timeout: Long,
        timeunit: TimeUnit,
        neededPermissions: List<Permission> = emptyList(),
        private val removeReaction: Boolean,
        private val disableMessageListening: Boolean
) : InteractableMessage(regnum, message, users, timeout, timeunit, neededPermissions) {

    private lateinit var future: CompletionStage<GuildMessageReactionAddEvent>

    override fun onMessage(event: GuildMessageReceivedEvent) {

    }

    override fun waitForInteraction() {
        if (!disableMessageListening) {
            super.waitForInteraction()
        }
        future = regnum.eventWaiter.waitFor(
                GuildMessageReactionAddEvent::class,
                {
                    val message = try {
                        it.channel.retrieveMessageById(it.messageIdLong).complete()
                    } catch (e: ErrorResponseException) {
                        SafeMessage.sendMessage("It looks like your message got deleted so the Interaction flow will get destroyed", it.channel).queue()
                        finish()
                        return@waitFor false
                    }
                    isUserAllowed(Context(it, message))
                },
                timeout,
                timeunit
        )
        future.thenAcceptAsync(Consumer {
            if (removeReaction) {

                it.reaction.removeReaction(it.user).queue()
            }
            handleReaction(it)
        }, executor)
    }

    override fun finish() {
        future.toCompletableFuture().cancel(true)
        super.finish()
    }

    /**
     * Method that gets invoked whenever the user performs a reaction-based interaction.
     * @param event the event of the reaction
     */
    abstract fun handleReaction(event: GuildMessageReactionAddEvent)

    override fun isUserAllowed(context: Context): Boolean {
        if (context.reaction != null && context.reaction.messageIdLong != message.idLong) {
            return false
        }

        return super.isUserAllowed(context)
    }
}