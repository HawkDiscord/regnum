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

import cc.hawkbot.regnum.client.command.permission.IPermissions

/**
 * Default sub-command class.
 *
 * @property displayName the display name for help messages
 * @property aliases the aliases of the command
 * @property usage the usage of the command
 * @property exampleUsage the example usage of the command
 * @property description the description of the command
 * @see ISubCommand
 */
@Suppress("unused")
abstract class SubCommand(
        override val displayName: String,
        override val aliases: Array<String>,
        private val _permissions: IPermissions? = null,
        override val usage: String = "",
        override val exampleUsage: String = "",
        override val description: String
) : ISubCommand {
    override val subCommandAssociations: MutableMap<String, ISubCommand>
        get() = throw UnsupportedOperationException("Sub commands do not support sub commands")

    override val permissions: IPermissions
        get() = _permissions ?: parent.permissions

    override lateinit var parent: ICommand
}