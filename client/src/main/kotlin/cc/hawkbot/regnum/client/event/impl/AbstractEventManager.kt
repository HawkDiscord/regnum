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

package cc.hawkbot.regnum.client.event.impl

import cc.hawkbot.regnum.client.event.EventManager
import cc.hawkbot.regnum.util.logging.Logger
import java.util.concurrent.ExecutorService

abstract class AbstractEventManager : EventManager {

    private val log = Logger.getLogger(EventManager::class.java)
    abstract val executor: ExecutorService
    abstract val listeners: MutableList<Any>
    override val registeredListeners: List<Any>
        // Make that list immutable
        get() = listeners.toList()

    override fun register(listener: Any) {
        this.listeners += listener
    }

    override fun unregister(listener: Any) {
        listeners.remove(listener)
    }

    fun fireEvent(action: () -> Unit, errorProcessor: (Throwable) -> Throwable = { it }) {
        executor.execute {
            try {
                action()
            } catch (e: Throwable) {
                log.error("[EventManager] One of the event listeners threw an error", errorProcessor(e))
            }
        }
    }
}