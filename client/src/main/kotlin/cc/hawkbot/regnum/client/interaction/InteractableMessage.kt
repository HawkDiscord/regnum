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

    abstract fun onMessage(event: GuildMessageReceivedEvent)

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

        fun author(): User {
            return member.user
        }

        fun guild(): Guild {
            return member.guild
        }

        fun channel(): TextChannel {
            return message.channel as TextChannel
        }

        fun deleteReaction(): RestAction<Void> {
            if (reaction == null) {
                throw UnsupportedOperationException("You cannot use reactions in a non reaction based message.")
            }
            return reaction.removeReaction(author())
        }
    }

}