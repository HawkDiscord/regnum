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

import cc.hawkbot.regnum.client.command.context.Arguments
import cc.hawkbot.regnum.client.command.context.Context
import cc.hawkbot.regnum.client.command.permission.IPermissions

/**
 * Interface for commands
 */
@Suppress("unused")
interface ICommand {

    /**
     * The group the command is in.
     */
    val group: Group

    /**
     * The display name of the command for help messages.
     */
    val displayName: String

    /**
     * The aliases of the command.
     */
    val aliases: Array<String>

    /**
     * The commands permissions.
     */
    val permissions: IPermissions

    /**
     * The commands usage without the prefix and the command name.
     */
    val usage: String

    /**
     * The commands example usage without the prefix and the command name.
     */
    val exampleUsage: String

    /**
     * The commands description.
     */
    val description: String

    /**
     * The alias associations of the sub-commands.
     */
    val subCommandAssociations: MutableMap<String, ISubCommand>

    /**
     * The method that gets executed when the command gets invoked
     */
    fun execute(args: Arguments, context: Context)

    /**
     * Returns the name of the command.
     * @return the name of the command
     */
    fun name(): String {
        return aliases[0]
    }

    /**
     * Registers a sub-command.
     * @param subCommands the sub-command
     */
    fun registerSubCommands(vararg subCommands: ISubCommand) {
        subCommands.forEach { registerSubCommand(it) }
    }

    /**
     * Registers a sub-command.
     * @param subCommand the sub-command
     */
    fun registerSubCommand(subCommand: ISubCommand) {
        subCommand.parent = this
        subCommand.aliases.forEach {
            subCommandAssociations[it] = subCommand
        }
    }
}