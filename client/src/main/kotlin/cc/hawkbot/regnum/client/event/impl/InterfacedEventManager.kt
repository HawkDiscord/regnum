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

import cc.hawkbot.regnum.util.DefaultThreadFactory
import cc.hawkbot.regnum.util.logging.Logger
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class InterfacedEventManager(
        override val executor: ExecutorService = Executors.newCachedThreadPool(DefaultThreadFactory("EventListeningPool"))
) : AbstractEventManager() {

    override val listeners = CopyOnWriteArrayList<Any>()
    override val registeredListeners: List<Any>
        // Make that list immutable
        get() = listeners.toList()

    override fun register(listener: Any) {
        if (listener !is EventListener) {
            throw IllegalArgumentException("Interfaced event manager only accepts EventListener implementing classes")
        }
        super.register(listener)
    }

    override fun unregister(listener: Any) {
        if (listener !is EventListener) {
            throw IllegalArgumentException("Interfaced event manager only accepts EventListener implementing classes")
        }
        super.unregister(listener)
    }

    override fun fireEvent(event: Any) {
        registeredListeners.asSequence().forEach {
            fireEvent {
                (it as EventListener).onEvent(event)
            }
        }
    }
}