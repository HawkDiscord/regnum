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

import cc.hawkbot.regnum.client.event.EventSubscriber
import cc.hawkbot.regnum.util.DefaultThreadFactory
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.superclasses
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class AnnotatedEventManger(
        override val executor: ExecutorService = Executors.newCachedThreadPool(DefaultThreadFactory("EventManager"))
) : AbstractEventManager() {
    override val listeners = CopyOnWriteArrayList<Any>()
    private val functions = ConcurrentHashMap<Any, MutableList<InstanceFunction>>()
    override val registeredListeners: List<Any>
        // Make that list immutable
        get() = listeners.toList()

    override fun register(listener: Any) {
        super.register(listener)
        updateMethods()

    }

    override fun register(vararg listeners: Any) {
        this.listeners.addAll(listeners)
        updateMethods()
    }

    override fun register(listeners: Collection<Any>) {
        if (this.listeners.addAll(listeners)) {
            updateMethods()
        }
    }

    override fun unregister(listener: Any) {
        super.unregister(listener)
        updateMethods()

    }

    private fun updateMethods() {
        functions.clear()
        listeners.forEach { it ->
            it::class.declaredFunctions.filterNot { it.findAnnotation<EventSubscriber>() == null }
                    .forEach { function ->
                        val parameters = function.valueParameters
                        if (parameters.isEmpty() || parameters.size > 1) {
                            throw IllegalArgumentException("EventSubscriber functions must have exactly one parameter (the event)")
                        }
                        val eventClass = parameters.first().type.jvmErasure
                        //println("REG $function")
                        val instanceFunction = InstanceFunction(it, function)
                        val functions = functions[eventClass]
                        if (functions == null) {
                            this.functions[eventClass] = mutableListOf(instanceFunction)
                        } else {
                            functions += instanceFunction
                        }
                    }
        }
    }

    override fun fireEvent(event: Any) {
        var eventClass: KClass<*>? = event::class
        while (eventClass != null) {
            val functions = this.functions[eventClass] ?: return
            fireEvent({
                functions.forEach {
                    it.call(event)
                }
            }, { InvocationTargetException(it) })
            eventClass = if (eventClass == Any::class) null else eventClass.superclasses.first()
        }

    }

    private data class InstanceFunction(private val instance: Any, private val function: KFunction<*>) {
        fun call(vararg parameters: Any?) {
            function.isAccessible = true
            function.call(instance, *parameters)
        }
    }
}