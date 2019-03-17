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

@file:Suppress("ObjectPropertyName", "FunctionName")

package cc.hawkbot.regnum.client.util

import cc.hawkbot.regnum.client.Regnum
import net.dv8tion.jda.api.JDA

private var _regnum: Regnum? = null

var JDA.regnum: Regnum
    get() = _regnum!!
    set(value) = _setRegnum(value)

internal fun _setRegnum(regnum: Regnum?) {
    if (regnum != null) {
        _regnum = regnum
    }
}
