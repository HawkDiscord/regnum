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
import net.dv8tion.jda.api.entities.IPermissionHolder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import java.util.concurrent.CompletionStage

/**
 * Manager for permissions.
 */
@Suppress("unused")
interface PermissionManager {

    /**
     * Returns whether a holder has a permission or not.
     * @param permissions the permissions
     * @param permissionHolder the holder
     * @return Whether the holder has the permission or not
     */
    fun hasPermissions(permissions: IPermissions, permissionHolder: IPermissionHolder): Boolean

    /**
     * Returns whether a member has a permission or not.
     * @param permissions the permissions
     * @param member the member
     * @return Whether the holder has the permission or not
     */
    fun hasPermissions(permissions: IPermissions, member: Member): Boolean

    /**
     * Returns whether a holder has all permissions or not.
     * @param permissionHolder the holder
     * @return Whether the holder has all permissions or not
     */
    fun hasWildcard(permissionHolder: IPermissionHolder): Boolean {
        return hasWildcard(permissionHolder.idLong, permissionHolder.guild.idLong)
    }

    /**
     * Returns whether a holder has all permissions or not.
     * @param id the id of the holder
     * @param guildId the id of the guild
     * @return Whether the holder has all permissions or not
     */
    fun hasWildcard(id: Long, guildId: Long): Boolean

    /**
     * Retrieves the permissions from the database.
     * @param info the [PermissionInfoContainer]
     * @return the [Result] containing the node
     */
    fun retrievePermissions(info: PermissionInfoContainer): Result<PermissionNode> {
        return retrievePermissions(info.id, info.guildId, info.node)
    }

    /**
     * Retrieves the permissions from the database.
     * @param id the id of the holder
     * @param guildId the id of the guild
     * @param node the permission node
     * @return the [Result] containing the node
     */
    fun retrievePermissions(id: Long, guildId: Long, node: String): Result<PermissionNode>

    /**
     * Creates a new permission node.
     * @param id the id of the holder
     * @param guildId the id of the guild
     * @param node the permission node
     * @param type the type of the holder
     * @param negated whether the permission should be negated or not
     * @return A [CompletionStage] containing the permission node
     */
    fun createPermissionNode(id: Long, guildId: Long, node: String, type: PermissionNode.PermissionTarget, negated: Boolean = false): CompletionStage<PermissionNode>

    /**
     * Creates a new permission node.
     * @param holder the permission holder
     * @param node the permission node
     * @param negated whether the permission should be negated or not
     * @return A [CompletionStage] containing the permission node
     */
    fun createPermissionNode(holder: IPermissionHolder, node: String, negated: Boolean = false): CompletionStage<PermissionNode> {
        return createPermissionNode(holder.idLong, holder.guild.idLong, node, if (holder is Role) PermissionNode.PermissionTarget.ROLE else PermissionNode.PermissionTarget.USER, negated)
    }

    /**
     * Returns a permission node.
     * @param snowflake the holder of the permission node
     * @param node the permission node
     * @return the node
     */
    fun getNode(snowflake: IPermissionHolder, node: String): PermissionNode {
        return getNode(snowflake.idLong, snowflake.guild.idLong, node)
    }

    /**
     * Returns a permission node.
     * @param id the id of the holder
     * @param guildId the id of the guild
     * @param node the permission node
     * @return the node
     */
    fun getNode(id: Long, guildId: Long, node: String): PermissionNode {
        return getNode(PermissionInfoContainer(id, guildId, node))
    }

    /**
     * Returns a permission node.
     * @param info the [PermissionInfoContainer]
     * @return the node
     */
    fun getNode(info: PermissionInfoContainer): PermissionNode

    /**
     * Returns all permission nodes of a holder in a [List].
     * @param permissionHolder the holder
     * @return all nodes in a list
     */
    fun getNodes(permissionHolder: IPermissionHolder): List<PermissionNode>

    /**
     * Returns whether the node exists or node.
     * @param snowflake the permission holder
     * @param node the node
     * @return whether the node exists or node
     */
    fun nodeExists(snowflake: IPermissionHolder, node: String): Boolean {
        return nodeExists(snowflake.idLong, snowflake.guild.idLong, node)
    }

    /**
     * Returns whether the node exists or node.
     * @param id the id of the holder
     * @param guildId the id of the guild
     * @param node the node
     * @return whether the node exists or node
     */
    fun nodeExists(id: Long, guildId: Long, node: String): Boolean {
        return nodeExists(PermissionInfoContainer(id, guildId, node))
    }

    /**
     * Returns whether the node exists or node.
     * @param info the [PermissionInfoContainer]
     * @return whether the node exists or node
     */
    fun nodeExists(info: PermissionInfoContainer): Boolean

    /**
     * Updates a node in cache.
     * @param node the node
     * @return A completed stage
     */
    fun updateNode(node: PermissionNode): CompletionStage<Void> {
        return updateNode(node.toInfo(), node)
    }

    /**
     * Updates a node in cache.
     * @param info the [PermissionInfoContainer] matching the node
     * @param node the node
     * @return A completed stage
     */
    fun updateNode(info: PermissionInfoContainer, node: PermissionNode): CompletionStage<Void>

    /**
     * Deletes a node.
     * @param node the node
     * @return A [CompletionStage] that finishes when the node got deleted
     */
    fun deleteNode(node: PermissionNode): CompletionStage<Void>

    /**
     * Checks whether a holder has the specified permission or not.
     * @param id the id of the holder
     * @param guildId the id of the guild
     * @param node the node
     * @param public whether its a public command or not
     * @return whether the holder has the permission or not
     */
    fun hasPermission(id: Long, guildId: Long, node: String, public: Boolean): Boolean {
        val container = PermissionInfoContainer(id, guildId, node, public)
        return hasPermission(container)
    }

    /**
     * Checks whether a holder has the specified permission or not.
     * @param info the [PermissionInfoContainer]
     * @return whether the holder has the permission or not
     */
    fun hasPermission(info: PermissionInfoContainer): Boolean

    /**
     * Container class for information about [PermissionNode]s
     * @property id the id of the holder
     * @property guildId the id of the guild
     * @property node the node
     * @property public whether its a public permission or not (only needed internally)
     */
    data class PermissionInfoContainer(
            val id: Long,
            val guildId: Long,
            val node: String,
            val public: Boolean = false
    ) {
        override fun equals(other: Any?): Boolean {
            if (other != null && other is PermissionInfoContainer) {
                return other.id == id && other.guildId == guildId && other.node == node
            }
            return false
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + guildId.hashCode()
            result = 31 * result + node.hashCode()
            result = 31 * result + public.hashCode()
            return result
        }
    }

}