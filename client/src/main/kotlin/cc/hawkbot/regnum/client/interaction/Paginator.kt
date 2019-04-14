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
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.function.Function
import cc.hawkbot.regnum.client.util.EmbedUtil
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission


/**
 * An Paginator implementation of [InteractableMessage]
 * @see PaginatorBuilder
 */
class Paginator<T>(
        regnum: Regnum,
        message: Message,
        users: List<User>,
        timeout: Long,
        timeunit: TimeUnit,
        private val content: LinkedList<T>,
        private val pageSize: Int,
        private val formatter: Function<T, String>,
        private val title: String

) : ReactableMessage(
        regnum,
        message,
        users,
        timeout,
        timeunit,
        listOf(Permission.MESSAGE_MANAGE, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EMBED_LINKS),
        true,
        true
) {

    private var currentPage = 1
    private var pageCount = Math.ceil(content.size.toDouble() / pageSize).toInt()

    init {
        if (pageCount == 1) {
            update()
        } else {
            CompletableFuture.allOf(
                    message.addReaction("▶").submit(),
                    message.addReaction("\uD83D\uDEAB").submit()
            ).thenAccept {
                update()
                retry()
            }
        }
    }

    override fun handleReaction(event: GuildMessageReactionAddEvent) {
        when (event.reaction.reactionEmote.name) {
            "◀" -> currentPage--
            "▶" -> currentPage++
            "\uD83D\uDEAB" -> finish()
            else -> return
        }

        val channel = event.channel
        val messageId = event.reaction.messageId
        var futures: Array<CompletableFuture<Void>> = arrayOf()
        if (currentPage >= pageCount) {
            futures += channel.removeReactionById(messageId, "▶").submit()
        }
        if (currentPage == 1) {
            futures += channel.removeReactionById(messageId, "◀").submit()
        }
        if (currentPage < pageCount) {
            futures += channel.addReactionById(messageId, "▶").submit()
        }
        if (currentPage > 1) {
            futures += channel.addReactionById(messageId, "◀").submit()
        }
        CompletableFuture.allOf(*futures).thenAccept {
            update()
            retry()
        }
    }

    private fun update() {
        editMessage(formatEmbed(title, formatCurrentPage())).queue()
    }
    private fun formatCurrentPage(): String {
        val start = (currentPage - 1) * pageSize
        val end = if (content.size < currentPage * pageSize) content.size else currentPage * pageSize
        val subList = content.subList(start, end)
        val pageBuilder = StringBuilder()
        subList.forEach { element -> pageBuilder.append(formatter.apply(element)).append("\n") }
        return pageBuilder.toString()
    }

    private fun formatEmbed(title: String, description: String): EmbedBuilder {
        return EmbedUtil.info(title, description)
                .setFooter(translate("phrases.list.footer").format(currentPage, pageCount), null)
    }

    override fun finish() {
        message.clearReactions().queue()
        super.finish()
    }

}