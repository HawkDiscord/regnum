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

import cc.hawkbot.regnum.client.command.context.Arguments

/**
 * Implementation of [Arguments]
 * @param array the array containing the args
 * @see Arguments
 * @constructor Constructs a new ArgumentsImpl
 */
@Suppress("unused")
class ArgumentsImpl(
        private val array: Array<String>
): Arguments {

    /**
     * @see Arguments.get
     */
    override fun get(index: Int): String {
        return array[index]
    }

    /**
     * @see Arguments.array
     */
    override fun array(): Array<String> {
        return array
    }
}