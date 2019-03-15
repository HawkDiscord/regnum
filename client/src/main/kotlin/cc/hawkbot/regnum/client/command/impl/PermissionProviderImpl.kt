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

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.command.permission.IPermissionProvider
import cc.hawkbot.regnum.client.command.permission.IPermissions
import cc.hawkbot.regnum.util.logging.Logger
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member

/**
 * Default implementation of [IPermissionProvider]
 */
@Suppress("unused")
class PermissionProviderImpl : IPermissionProvider {

    private val log = Logger.getLogger()
    override lateinit var regnum: Regnum

    override fun hasPermission(permissions: IPermissions, member: Member): Boolean {
        // Public command
        if (permissions.public) {
            return true
        }

        // Check lateinit var
        if (!this::regnum.isInitialized) {
            log.error("[PermissionProvider] Regnum instance has not been injected yet")
            return false
        }

        if (member.user.idLong in regnum.owners) {
            return true
        }

        if (permissions.botOwnerExclusive) {
            return false
        }

        val isAdmin = member.isOwner ||
                member.hasPermission(Permission.MANAGE_SERVER)
        if (isAdmin)
            return true
        return verifyNode(permissions, member)
    }

    private fun verifyNode(permissions: IPermissions, member: Member): Boolean {
        return regnum.permissionManager.hasPermissions(permissions, member)
    }
}