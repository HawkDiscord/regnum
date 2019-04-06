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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

/**
 * Regnum command parser
 */
interface CommandParser {

    /**
     * The prefix that is valid on all guilds.
     */
    val defaultPrefix: String

    /**
     * A list of user ids who has all permissions.
     */
    val owners: List<Long>

    /**
     * A map containing all aliases and its commands.
     */
    val commandAssociations: Map<String, ICommand>

    /**
     * A list of all commands.
     */
    val commands: List<ICommand>
        get() = commandAssociations.values.distinct()

    /**
     * Returns all commands and its aliases in a map.
     * @return all commands and its aliases in a map
     */
    @Deprecated("We're replacing fluent getters with Kotlin fields", ReplaceWith("commandAssociations"))
    fun commands(): Map<String, ICommand> {
        return commandAssociations
    }

    /**
     * Registers a command
     * @param command the command
     */
    fun registerCommand(command: ICommand)

    /**
     * Registers a command
     * @param commands the command
     */
    fun registerCommands(vararg commands: ICommand) {
        commands.forEach { registerCommand(it) }
    }

    /**
     * Un-registers a command
     * @param command the command
     */
    fun unregisterCommand(command: ICommand)

    /**
     * Method that handles incoming messages.
     * @param event the message event
     */
    fun receiveMessage(event: GuildMessageReceivedEvent)
}