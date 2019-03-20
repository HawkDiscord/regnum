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

package cc.hawkbot.regnum.client.config

import cc.hawkbot.regnum.client.command.ICommand
import cc.hawkbot.regnum.client.command.impl.PermissionProviderImpl
import cc.hawkbot.regnum.client.command.permission.IPermissionProvider
import cc.hawkbot.regnum.client.command.translation.DefaultLanguageManager
import cc.hawkbot.regnum.client.command.translation.Language
import cc.hawkbot.regnum.client.command.translation.LanguageManager
import cc.hawkbot.regnum.util.DefaultThreadFactory
import java.util.*
import java.util.concurrent.Executors

/**
 * Config for all command system related settings.
 * @property defaultPrefix the default prefix
 * @property permissionProvider the permission provider
 * @property commands a list of commands
 * @property botOwners a list of ids which should have all permissions
 * @property languageManager the language manager
 * @constructor constructs a new CommandConfig
 */
data class CommandConfig(
        val defaultPrefix: String,
        val permissionProvider: IPermissionProvider,
        val commands: MutableList<ICommand>,
        val botOwners: MutableList<Long>,
        val languageManager: LanguageManager
) {

    /**
     * Executor for commands.
     */
    val executor = Executors.newCachedThreadPool(DefaultThreadFactory("CommandExecutor"))!!

    /**
     * Config for all command system related settings.
     * @property defaultPrefix the default prefix
     * @property commands a list of commands
     * @property botOwners a list of ids which should have all permissions
     * @property languageManager the language manager
     * @constructor constructs a new CommandConfig using [PermissionProviderImpl]
     */
    constructor(
            defaultPrefix: String,
            commands: MutableList<ICommand>,
            botOwners: MutableList<Long>,
            languageManager: LanguageManager
    ) : this(defaultPrefix, PermissionProviderImpl(), commands, botOwners, languageManager)

    /**
     * Config for all command system related settings.
     * @property defaultPrefix the default prefix
     * @property languageManager the language manager
     * @constructor constructs a new CommandConfig using [PermissionProviderImpl] and empty lists for [commands] and [botOwners]
     */
    constructor(
            defaultPrefix: String,
            languageManager: LanguageManager
    ) : this(defaultPrefix, mutableListOf(), mutableListOf(), languageManager)

    /**
     * Config for all command system related settings.
     * @property defaultPrefix the default prefix
     * @param defaultLanguage the default [Language]
     * @constructor constructs a new CommandConfig using [PermissionProviderImpl] and empty lists for [commands] and [botOwners] and [DefaultLanguageManager]
     */
    constructor(defaultPrefix: String, defaultLanguage: Language) : this(defaultPrefix, DefaultLanguageManager(Language.defaultLanguage(defaultLanguage)))


    /**
     * Registers a command.
     * @param commands the command
     * @return the builder
     */
    fun registerCommands(vararg commands: ICommand): CommandConfig {
        Collections.addAll(this.commands, *commands)
        return this
    }

    /**
     * Registers a command.
     * @param commands the command
     * @return the builder
     */
    fun registerCommands(commands: Collection<ICommand>): CommandConfig {
        this.commands.addAll(commands)
        return this
    }

    /**
     * Assigns an user owner permissions
     * @param ownerIds the id of the user
     * @return the builder
     */
    fun addBotOwners(vararg ownerIds: Long): CommandConfig {
        Collections.addAll<Long>(botOwners, *ownerIds.toTypedArray())
        return this
    }

    /**
     * Assigns an user owner permissions
     * @param ownerIds the id of the user
     * @return the builder
     */
    fun addBotOwners(ownerIds: Collection<Long>): CommandConfig {
        botOwners.addAll(ownerIds)
        return this
    }
}