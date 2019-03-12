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

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.entities.permissions.PermissionNode
import com.datastax.driver.mapping.Result
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.ISnowflake
import net.dv8tion.jda.api.entities.User
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit

@Suppress("MemberVisibilityCanBePrivate")
class PermissionManager(
        regnum: Regnum
) {

    private val cassandra = regnum.cassandra
    private val accessor: PermissionNode.Accessor
    private val cache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(object : CacheLoader<PermissionInfoContainer, PermissionNode>() {
                override fun load(key: PermissionInfoContainer): PermissionNode {
                    val result = retrievePermissions(key)
                    if (result.availableWithoutFetching == 0)
                        throw NullPointerException("Permission node not found")
                    val node = result.one()
                    node.manager = this@PermissionManager
                    return node
                }
            })

    init {
        accessor = cassandra.mappingManager.createAccessor(PermissionNode.Accessor::class.java)
    }

    private fun retrievePermissions(info: PermissionInfoContainer): Result<PermissionNode> {
        return retrievePermissions(info.id, info.guildId, info.node)
    }

    private fun retrievePermissions(id: Long, guildId: Long, node: String): Result<PermissionNode> {
        return accessor.getNode(id, guildId, node)
    }

    fun createPermissionNode(id: Long, guildId: Long, node: String, type: PermissionNode.PermissionTarget, negated: Boolean = false): CompletionStage<PermissionNode> {
        val permissionNode = PermissionNode(id, negated, guildId, node, type)
        permissionNode.manager = this
        return permissionNode.saveAsync().thenApply { permissionNode }
    }

    fun getNode(snowflake: ISnowflake, guild: Guild, node: String): PermissionNode {
        return getNode(snowflake.idLong, guild.idLong, node)
    }

    fun getNode(id: Long, guildId: Long, node: String): PermissionNode {
        return getNode(PermissionInfoContainer(id, guildId, node))
    }

    fun getNode(info: PermissionInfoContainer): PermissionNode {
        return cache[info]
    }

    fun nodeExists(snowflake: ISnowflake, guild: Guild, node: String): Boolean {
        return nodeExists(snowflake.idLong, guild.idLong, node)
    }

    fun nodeExists(id: Long, guildId: Long, node: String): Boolean {
        return nodeExists(PermissionInfoContainer(id, guildId, node))
    }

    fun nodeExists(info: PermissionInfoContainer): Boolean {
        return try {
            getNode(info)
            true
        } catch (e: NullPointerException) {
            false
        }
    }

    fun updateNode(node: PermissionNode): CompletionStage<Void> {
        return updateNode(node.toInfo(), node)
    }

    fun updateNode(info: PermissionInfoContainer, node: PermissionNode): CompletionStage<Void> {
        cache.put(info, node)
        return node.saveAsync()
    }

    fun deleteNode(node: PermissionNode): CompletionStage<Void> {
        cache.invalidate(node.toInfo())
        return node.deleteAsync()
    }

    fun hasPermission(id: Long, guildId: Long, node: String): Boolean {
        return nodeExists(id, guildId, node) && !getNode(id, guildId, node).isNegated
    }

    fun hasPermission(info: PermissionInfoContainer): Boolean {
        return nodeExists(info) && !getNode(info).isNegated
    }

    data class PermissionInfoContainer(
            val id: Long,
            val guildId: Long,
            val node: String
    )
}