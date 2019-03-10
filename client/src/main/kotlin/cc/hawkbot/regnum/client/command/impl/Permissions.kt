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

import cc.hawkbot.regnum.client.command.permission.IPermissions

/**
 * Generic implementation of [IPermissions].
 * @param botOwnerExclusive whether only the bot owner should have the permission or not
 * @param serverAdminExclusive whether only the server admin should have the permission or not
 * @param public whether everyone should have the permission or not
 * @param node the permission node of the command
 * @constructor Constructs a new permission object
 */
@Suppress("unused")
open class Permissions(override val botOwnerExclusive: Boolean = false, override val serverAdminExclusive: Boolean = true, override val public: Boolean = false, override val node: String) : IPermissions