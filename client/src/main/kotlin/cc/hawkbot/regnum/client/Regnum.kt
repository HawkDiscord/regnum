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

package cc.hawkbot.regnum.client

import cc.hawkbot.regnum.client.core.Websocket
import cc.hawkbot.regnum.client.core.discord.Discord
import cc.hawkbot.regnum.client.event.EventManager
import cc.hawkbot.regnum.waiter.EventWaiter

/**
 * Main class of Regnum client instance.
 */
@Suppress("unused")
interface Regnum {

    /**
     * The websocket instance.
     */
    val websocket: Websocket

    /**
     * The discord instance.
     */
    val discord: Discord

    /**
     * The event manager.
     */
    val eventManager: EventManager

    /**
     * The token used for authentication.
     */
    val token: String

    /**
     * The event waiter.
     */
    val eventWaiter: EventWaiter

    /**
     * A list of all disabled features.
     */
    val disabledFeatures: List<Feature>

}