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

package cc.hawkbot.regnum.server.plugin

import cc.hawkbot.regnum.server.plugin.core.AuthorizationHandler
import cc.hawkbot.regnum.server.plugin.discord.DiscordBot
import cc.hawkbot.regnum.server.plugin.io.config.Config
import cc.hawkbot.regnum.waiter.impl.EventWaiter
import io.javalin.Javalin
import net.dv8tion.jda.api.hooks.IEventManager
import java.io.Closeable

interface Server: Closeable {

    val launchedAt: Long

    val dev: Boolean

    val javalin: Javalin

    val websocket: Websocket

    val config: Config

    val discordBot: DiscordBot

    val eventWaiter: EventWaiter

    val eventManager: IEventManager

    var authorizationHandler: AuthorizationHandler
}