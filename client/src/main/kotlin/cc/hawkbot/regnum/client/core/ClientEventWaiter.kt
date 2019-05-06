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

package cc.hawkbot.regnum.client.core

import cc.hawkbot.regnum.client.event.EventManager
import cc.hawkbot.regnum.client.event.EventSubscriber
import cc.hawkbot.regnum.client.event.impl.EventListener
import cc.hawkbot.regnum.waiter.EventWaiter
import java.util.concurrent.*
import java.util.function.Predicate

/**
 * Implementation of [EventWaiter].
 * @param eventManager the event manager
 */
class ClientEventWaiter(
        private val eventManager: EventManager
) : EventWaiter {

    private val scheduler = Executors.newScheduledThreadPool(10)

    override fun <T> waitFor(eventClass: Class<T>, predicate: Predicate<T>, timeout: Long, timeoutUnit: TimeUnit): CompletionStage<T> {
        val future = CompletableFuture<T>()
        val listener = object : WaitingEventListener() {
            @EventSubscriber
            @Suppress("unused")
            override fun onEvent(event: Any) {
                @Suppress("UNCHECKED_CAST")
                if (eventClass.isInstance(event)) {
                    val foundEvent = event as T
                    if (predicate.test(foundEvent)) {
                        cancelTimeout()
                        unregister(eventManager)
                        future.complete(foundEvent)
                    }
                }
            }
        }
        val timeoutFuture = scheduler.schedule({
            eventManager.unregister(eventManager)
            future.completeExceptionally(EventWaiter.TimeoutException("Timeout exceeded"))
        }, timeout, timeoutUnit)
        listener.future = timeoutFuture
        eventManager.register(listener)
        return future
    }

    private abstract class WaitingEventListener : EventListener {

        lateinit var future: ScheduledFuture<*>

        fun cancelTimeout() {
            future.cancel(true)
        }

        fun unregister(eventManager: EventManager) {
            eventManager.unregister(this)
        }
    }

    override fun close() = scheduler.shutdownNow().run { Unit }
}