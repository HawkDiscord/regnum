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

package cc.hawkbot.regnum.client.command.context

/**
 * Represents the parsed [Arguments] of a command.
 */
@Suppress("unused")
interface Arguments {

    /**
     * An array containing all arguments.
     */
    val array: Array<String>

    /**
     * The size of the arguments.
     */
    val size: Int
        get() = array.size

    /**
     * Returns the element at the specified [index].
     * @param index the index
     * @see Array.get
     * @return the element
     */
    operator fun get(index: Int): String

    /**
     * Returns the amounts of arguments.
     * @return the amounts of arguments
     */
    @Deprecated("We're moving fluent getters to Kotlin fields", ReplaceWith("size"))
    fun size(): Int {
        return size
    }

    /**
     * Returns whether there are arguments or not.
     * @return whether there are arguments or not
     */
    fun isEmpty(): Boolean {
        return array.isEmpty()
    }

    /**
     * Converts the arguments into an array.
     * @return the arguments as an array
     */
    @Deprecated("We're moving fluent getters to Kotlin fields", ReplaceWith("array"))
    fun array(): Array<String> {
        return array
    }

    /**
     * Converts to arguments from the [start] to the [end] into an array.
     * @param start the start parameter
     * @param end the end parameter
     * @see Array.sliceArray
     * @return the array
     */
    fun array(start: Int, end: Int): Array<String> {
        return array.sliceArray(start until end)
    }

    /**
     * Converts all arguments from the begin of the arguments to the [end] in an array.
     * @param end the end parameter
     * @see Arguments.array
     * @return the array
     */
    fun arrayTo(end: Int): Array<String> {
        return array(0, end)
    }

    /**
     * Converts all arguments from the [start] to the end of the arguments.
     * @param start the start parameter
     * @see Arguments.array
     * @return the array
     */
    fun arrayFrom(start: Int): Array<String> {
        return array(start, size)
    }

    /**
     * Creates a string from all the elements separated using [separator] and using the given [prefix] and [postfix] if supplied.
     *
     * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
     * elements will be appended, followed by the [truncated] string (which defaults to "...").
     */
    fun <T> string(separator: CharSequence = " ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((T) -> CharSequence)? = null): String {
        return array.joinToString(separator, prefix, postfix, limit, truncated)
    }

    /**
     * Creates a string from all the elements from the [start] to the [end] separated using [separator] and using the given [prefix] and [postfix] if supplied.
     *
     * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
     * elements will be appended, followed by the [truncated] string (which defaults to "...").
     */
    fun <T> string(start: Int, end: Int, separator: CharSequence = " ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((T) -> CharSequence)? = null): String {
        return array(start, end).joinToString(separator, prefix, postfix, limit, truncated)
    }

    /**
     * Creates a string from all the elements from the start to the [end] separated using [separator] and using the given [prefix] and [postfix] if supplied.
     *
     * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
     * elements will be appended, followed by the [truncated] string (which defaults to "...").
     */
    fun <T> stringTo(end: Int, separator: CharSequence = " ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((T) -> CharSequence)? = null): String {
        return arrayTo(end).joinToString(separator, prefix, postfix, limit, truncated)
    }

    /**
     * Creates a string from all the elements from the [start] to the end separated using [separator] and using the given [prefix] and [postfix] if supplied.
     *
     * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
     * elements will be appended, followed by the [truncated] string (which defaults to "...").
     */
    fun <T> stringFrom(start: Int, separator: CharSequence = " ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((T) -> CharSequence)? = null): String {
        return arrayTo(start).joinToString(separator, prefix, postfix, limit, truncated)
    }
}