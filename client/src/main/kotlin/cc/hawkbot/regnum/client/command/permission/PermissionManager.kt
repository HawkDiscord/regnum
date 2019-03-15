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

package cc.hawkbot.regnum.client.command.permission

import cc.hawkbot.regnum.client.entities.permission.PermissionNode
import com.datastax.driver.mapping.Result
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.entities.IPermissionHolder
import java.util.concurrent.CompletionStage

interface PermissionManager {

    fun hasPermissions(permissions: IPermissions, member: Member): Boolean {
        return hasPermissions(permissions.node, member)
    }

    fun hasPermissions(node: String, member: Member): Boolean

    fun hasPermissions(permissions: IPermissions, permissionHolder: IPermissionHolder): Boolean {
        return hasPermissions(permissions.node, permissionHolder)
    }

    fun hasPermissions(node: String, permissionHolder: IPermissionHolder): Boolean

    fun hasWildcard(permissionHolder: IPermissionHolder): Boolean {
        return hasWildcard(permissionHolder.idLong, permissionHolder.guild.idLong)
    }

    fun hasWildcard(id: Long, guildId: Long): Boolean

    fun retrievePermissions(info: PermissionInfoContainer): Result<PermissionNode> {
        return retrievePermissions(info.id, info.guildId, info.node)
    }

    fun retrievePermissions(id: Long, guildId: Long, node: String): Result<PermissionNode>

    fun createPermissionNode(id: Long, guildId: Long, node: String, type: PermissionNode.PermissionTarget, negated: Boolean = false): CompletionStage<PermissionNode>

    fun createPermissionNode(holder: IPermissionHolder, node: String, negated: Boolean = false): CompletionStage<PermissionNode> {
        return createPermissionNode(holder.idLong, holder.guild.idLong, node, if (holder is Role) PermissionNode.PermissionTarget.ROLE else PermissionNode.PermissionTarget.USER, negated)
    }

    fun getNode(snowflake: IPermissionHolder, node: String): PermissionNode {
        return getNode(snowflake.idLong, snowflake.guild.idLong, node)
    }

    fun getNode(id: Long, guildId: Long, node: String): PermissionNode {
        return getNode(PermissionInfoContainer(id, guildId, node))
    }

    fun getNode(info: PermissionInfoContainer): PermissionNode

    fun getNodes(permissionHolder: IPermissionHolder): List<PermissionNode>

    fun nodeExists(snowflake: IPermissionHolder, node: String): Boolean {
        return nodeExists(snowflake.idLong, snowflake.guild.idLong, node)
    }

    fun nodeExists(id: Long, guildId: Long, node: String): Boolean {
        return nodeExists(PermissionInfoContainer(id, guildId, node))
    }

    fun nodeExists(info: PermissionInfoContainer): Boolean

    fun updateNode(node: PermissionNode): CompletionStage<Void> {
        return updateNode(node.toInfo(), node)
    }

    fun updateNode(info: PermissionInfoContainer, node: PermissionNode): CompletionStage<Void>

    fun deleteNode(node: PermissionNode): CompletionStage<Void>

    fun hasPermission(id: Long, guildId: Long, node: String): Boolean {
        return nodeExists(id, guildId, node) && !getNode(id, guildId, node).isNegated
    }

    fun hasPermission(info: PermissionInfoContainer): Boolean

    data class PermissionInfoContainer(
            val id: Long,
            val guildId: Long,
            val node: String
    )

}