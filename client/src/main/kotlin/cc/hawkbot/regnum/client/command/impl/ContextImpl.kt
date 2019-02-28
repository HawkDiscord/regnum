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

package cc.hawkbot.regnum.client.command.impl

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.command.ICommand
import cc.hawkbot.regnum.client.command.context.Arguments
import cc.hawkbot.regnum.client.command.context.Context
import cc.hawkbot.regnum.client.command.context.Mentions
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

/**
 * Implementation of [Context].
 * @param event the message event
 * @param command the command
 * @param mentions the mentions object
 * @param regnum the regnum instance
 * @see Context
 * @constructor Constructs a new ContextImpl
 */
@Suppress("unused")
class ContextImpl(override val event: GuildMessageReceivedEvent, override val command: ICommand, override val args: Arguments, override val mentions: Mentions, override val regnum: Regnum) : Context