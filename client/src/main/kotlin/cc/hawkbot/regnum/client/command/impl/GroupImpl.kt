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

import cc.hawkbot.regnum.client.command.Group
import cc.hawkbot.regnum.client.command.groups
import cc.hawkbot.regnum.client.command.permission.IPermissions

/**
 * Implementation of [Group].
 * @param public whether the group should be listed in the help command or not
 * @param name the name of the group
 * @param description the description of the group
 * @param permissions the group-wide permissions
 * @constructor Constructs a new group
 */
class GroupImpl(override val public: Boolean, override val name: String, override val description: String, override val permissions: IPermissions) : Group {

    init {
        if (this !in groups) {
            groups.add(this)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is Group && other.name == this.name
    }

    override fun hashCode(): Int {
        var result = public.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + permissions.hashCode()
        return result
    }
}