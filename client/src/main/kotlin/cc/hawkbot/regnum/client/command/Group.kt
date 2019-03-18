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

package cc.hawkbot.regnum.client.command

import cc.hawkbot.regnum.client.command.permission.GroupPermissions
import cc.hawkbot.regnum.client.command.permission.IPermissions

/**
 * List of all groups
 */
val groups = mutableListOf<Group>()

/**
 * Interface for commands groups.
 */
@Suppress("unused")
interface Group {

    /**
     * Whether the group should be visible in the help or not.
     */
    val public: Boolean

    /**
     * The name of the group.
     */
    val name: String

    /**
     * The description of the group.
     */
    val description: String

    /**
     * Group wide command permissions
     */
    val permissions: IPermissions

    /**
     * Returns a list of all commands with that group.
     * @return a list of all commands with that group
     */
    fun commands(commandParser: CommandParser): Collection<ICommand> {
        return commandParser.commands().filter { it.value.group == this }.values
    }

    companion object {

        /**
         * Settings group
         */
        val SETTINGS = GroupBuilder()
                .setDescription("Commands to configure your server")
                .setName("Settings")
                .setPermissions(GroupPermissions(node = "settings", serverAdminExclusive = true))
                .build()

        val GENERAL = GroupBuilder()
                .setDescription("Generic commands")
                .setName("General")
                .setPermissions(GroupPermissions(node = "general", public = true))
                .build()

        /**
         * Returns an empty group.
         * @return an empty group
         */
        fun empty(): Group {
            return object : Group {
                override val public: Boolean
                    get() = false
                override val name: String
                    get() = "none"
                override val description: String
                    get() = "none"
                override val permissions = GroupPermissions(
                        true,
                        false,
                        false,
                        "none"
                )
            }
        }
    }
}