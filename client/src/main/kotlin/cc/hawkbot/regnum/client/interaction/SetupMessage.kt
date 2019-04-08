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
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import java.util.concurrent.TimeUnit

abstract class SetupMessage(regnum: Regnum, message: Message, users: List<User>, timeout: Long, timeunit: TimeUnit, neededPermissions: List<Permission> = emptyList(), removeReaction: Boolean, disableMessageListening: Boolean) : ReactableMessage(regnum, message, users, timeout, timeunit, neededPermissions, removeReaction, disableMessageListening) {

    private var step = 1
    private val user: User
        get() = users[0]

    override fun handleReaction(event: GuildMessageReactionAddEvent) {
        handleStep(event, step)
    }

    override fun onMessage(event: GuildMessageReceivedEvent) {
        handleStep(event, step)
    }

    abstract fun handleStep(event: GuildMessageReceivedEvent, step: Int)

    abstract fun handleStep(event: GuildMessageReactionAddEvent, step: Int)

    protected fun next(step: Int = 1) {
        this.step += step
        super.retry()
    }

    override fun retry() {
        next(0)
    }
}