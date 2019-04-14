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
import cc.hawkbot.regnum.util.DefaultThreadFactory
import cc.hawkbot.regnum.waiter.EventWaiter
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.requests.restaction.MessageAction
import java.util.concurrent.CompletionStage
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Function

/**
 * Message which listens for interactions from the user.
 * @property regnum the current regnum instance
 * @property message the message which should be listened on
 * @property users a list of users who are allowed to interact with the message
 * @property timeout the time after the message should stop to listen for interaction
 * @property timeunit the unit for the timeout
 * @param neededPermissions a list of permissions the message needs to run
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class InteractableMessage(
        protected val regnum: Regnum,
        protected val message: Message,
        protected val users: List<User>,
        protected val timeout: Long,
        protected val timeunit: TimeUnit,
        neededPermissions: List<Permission> = emptyList()
) {

    private lateinit var future: CompletionStage<GuildMessageReceivedEvent>

    companion object {
        /**
         * Scheduler for interactable messages
         */
        val executor = Executors.newCachedThreadPool(DefaultThreadFactory("Interaction"))!!
    }

    init {
        run {
            if (!message.guild.selfMember.hasPermission(message.channel as GuildChannel, neededPermissions)) {
                editMessage(
                        EmbedUtil.error(
                                translate("phrases.nopermission.title"),
                                translate("phrases.nopermission.description")
                                        .format(
                                                neededPermissions.joinToString(prefix = "`", separator = "`, `", postfix = "`")
                                                { it.toString() }
                                        )
                        )
                ).queue()
                return@run
            }
        }
    }

    /**
     * Method that gets invoked whenever the user performs a message interaction.
     * @param event the event of the message
     */
    abstract fun onMessage(event: GuildMessageReceivedEvent)

    /**
     * Method that waits for user interaction
     */
    protected open fun waitForInteraction() {
        future = regnum.eventWaiter.waitFor(
                GuildMessageReceivedEvent::class.java,
                { isUserAllowed(Context(it)) },
                timeout,
                timeunit
        )
        future.thenAcceptAsync(Consumer {
            onMessage(it)
        }, executor)
        future.exceptionallyAsync (Function {
            if (it is EventWaiter.TimeoutException) {
                editMessage(
                        EmbedUtil.error(
                                translate("phrases.interactable.timeout.title"),
                                translate("phrases.interactable.timeout.description")
                        )
                ).queue()
            }
            null
        },
                executor)
    }

    protected open fun retry() {
        waitForInteraction()
    }

    protected fun editMessage(message: Message): MessageAction {
        return SafeMessage.editMessage(this.message, message
        ) { notifyUser() }
    }

    protected fun editMessage(messageBuilder: MessageBuilder): MessageAction {
        return editMessage(messageBuilder.build())
    }

    protected fun editMessage(content: String): MessageAction {
        return editMessage(MessageBuilder(content))
    }

    protected fun editMessage(embed: MessageEmbed): MessageAction {
        return editMessage(MessageBuilder(embed))
    }

    protected fun editMessage(embedBuilder: EmbedBuilder): MessageAction {
        return editMessage(embedBuilder.build())
    }

    protected fun translate(key: String): String {
        return TranslationUtil.translate(regnum, key, users[0])
    }

    protected open fun isUserAllowed(context: Context): Boolean {
        val user = context.author()
        if (user.isBot || user.isFake || context.message.isWebhookMessage) {
            return false
        }
        return user in users
    }

    protected open fun finish() {
        future.toCompletableFuture().cancel(true)
    }

    private fun notifyUser() {
        users.forEach {
            it.openPrivateChannel().queue {channel ->
                channel.sendMessage("Could not handle your request please make sure that I have message permissions!").queue()
            }
        }
    }

    /**
     * Container for [member], [message] and [reaction] of an interaction
     */
    data class Context(
            val member: Member,
            val message: Message,
            val reaction: MessageReaction?
    ) {
        constructor(event: GuildMessageReceivedEvent) : this(event.member, event.message, null)
        constructor(event: GuildMessageReactionAddEvent) : this(
                event.member,
                event.channel.retrieveMessageById(event.messageIdLong).complete(),
                event.reaction
        )

        /**
         * The author of the interaction.
         */
        @Suppress("unused")
        val author: User
            get() = member.user

        /**
         * The guild the interaction was performed on.
         */
        val guild: Guild
            get() = member.guild

        /**
         * The channel the interaction was performed in.
         */
        val channel: TextChannel
            get() = message.channel as TextChannel

        /**
         * The author of the interaction.
         */
        @Deprecated("We moving from fluent getters to Kotlin fields", ReplaceWith("author"))
        fun author(): User {
            return member.user
        }

        /**
         * The guild the interaction was performed on.
         */
        @Deprecated("We moving from fluent getters to Kotlin fields", ReplaceWith("guild"))
        fun guild(): Guild {
            return member.guild
        }

        /**
         * The channel the interaction was performed in.
         */
        @Deprecated("We moving from fluent getters to Kotlin fields", ReplaceWith("channel"))
        fun channel(): TextChannel {
            return message.channel as TextChannel
        }

        /**
         * @return a RestAction which deletes the reaction
         */
        @Suppress("unused")
        fun deleteReaction(): RestAction<Void> {
            if (reaction == null) {
                throw UnsupportedOperationException("You cannot use reactions in a non reaction based message.")
            }
            return reaction.removeReaction(author)
        }
    }

}