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

package cc.hawkbot.regnum.client.core.internal

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.command.CommandParser
import cc.hawkbot.regnum.client.command.ICommand
import cc.hawkbot.regnum.client.command.impl.CommandParserImpl
import cc.hawkbot.regnum.client.command.permission.IPermissionProvider
import cc.hawkbot.regnum.client.core.Websocket
import cc.hawkbot.regnum.client.core.discord.Discord
import cc.hawkbot.regnum.client.core.discord.GameAnimator
import net.dv8tion.jda.api.hooks.IEventManager
import java.util.function.Function

/**
 * Implementation of [Regnum]
 * @param host the host of the Regnum server
 * @property eventManager the event manager
 * @property token the Regnum token
 * @property games a list of games for the [GameAnimator]
 * @property gameAnimatorInterval the interval for the [GameAnimator]
 * @see cc.hawkbot.regnum.client.RegnumBuilder
 * @constructor Constructs a new Regnum instance
 */
class RegnumImpl(
        host: String,
        override val eventManager: IEventManager,
        override val token: String,
        val games: MutableList<GameAnimator.Game>,
        val gameTranslator: Function<String, String>,
        val gameAnimatorInterval: Long,
        permissionProvider: IPermissionProvider,
        defaultPrefix: String,
        commands: List<ICommand>,
        override val owners: List<Long>
) : Regnum {

    override val websocket: Websocket
    override lateinit var discord: Discord
    override val commandParser: CommandParser

    init {
        permissionProvider.regnum = this
        commandParser = CommandParserImpl(
                defaultPrefix,
                permissionProvider,
                this
        )
        commandParser.registerCommands(*commands.toTypedArray())
        eventManager.register(HeartBeater())
        eventManager.register(PacketHandler(this))
        eventManager.register(commandParser)
        websocket = WebsocketImpl(host, this)
        try {
            websocket.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}