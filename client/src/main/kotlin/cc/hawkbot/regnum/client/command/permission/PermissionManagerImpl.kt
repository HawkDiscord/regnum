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
import cc.hawkbot.regnum.client.entities.permission.PermissionNode
import com.datastax.driver.mapping.Result
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import net.dv8tion.jda.api.entities.IPermissionHolder
import net.dv8tion.jda.api.entities.Member
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit

@Suppress("MemberVisibilityCanBePrivate", "unused")
class PermissionManagerImpl(
        regnum: Regnum
) : PermissionManager {

    private val cassandra = regnum.cassandra
    private val accessor: PermissionNode.Accessor
    private val cache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(object : CacheLoader<PermissionManager.PermissionInfoContainer, PermissionNode>() {
                override fun load(key: PermissionManager.PermissionInfoContainer): PermissionNode {
                    val result = retrievePermissions(key)
                    if (result.availableWithoutFetching == 0)
                        throw NullPointerException("Permission node not found")
                    val node = result.one()
                    node.manager = this@PermissionManagerImpl
                    return node
                }
            })

    init {
        accessor = cassandra.mappingManager.createAccessor(PermissionNode.Accessor::class.java)
    }

    override fun hasPermissions(node: String, member: Member): Boolean {
        if (hasPermissions(node, permissionHolder = member)) {
            return true
        }
        member.roles.forEach {
            if (hasPermissions(node, it))
                return true
        }
        return false
    }

    override fun hasPermissions(node: String, permissionHolder: IPermissionHolder): Boolean {
        val container = PermissionManager.PermissionInfoContainer(permissionHolder.idLong, permissionHolder.guild.idLong, node)
        return hasPermission(container)
    }

    override fun hasWildcard(id: Long, guildId: Long): Boolean {
        return hasPermission(id, guildId, "*")
    }

    override fun retrievePermissions(id: Long, guildId: Long, node: String): Result<PermissionNode> {
        return accessor.getNode(id, guildId, node)
    }

    override fun createPermissionNode(id: Long, guildId: Long, node: String, type: PermissionNode.PermissionTarget, negated: Boolean): CompletionStage<PermissionNode> {
        val permissionNode = PermissionNode(id, negated, guildId, node, type)
        permissionNode.manager = this
        return permissionNode.saveAsync().thenApply { permissionNode }
    }

    override fun getNode(info: PermissionManager.PermissionInfoContainer): PermissionNode {
        return cache[info]
    }

    override fun getNodes(permissionHolder: IPermissionHolder): List<PermissionNode> {
        return accessor.getNodes(permissionHolder.idLong, permissionHolder.guild.idLong).all()
    }

    override fun nodeExists(info: PermissionManager.PermissionInfoContainer): Boolean {
        return try {
            getNode(info)
            true
        } catch (e: NullPointerException) {
            false
        }
    }

    override fun updateNode(info: PermissionManager.PermissionInfoContainer, node: PermissionNode): CompletionStage<Void> {
        cache.put(info, node)
        return node.saveAsync()
    }

    override fun deleteNode(node: PermissionNode): CompletionStage<Void> {
        cache.invalidate(node.toInfo())
        return node.deleteAsync()
    }

    override fun hasPermission(info: PermissionManager.PermissionInfoContainer): Boolean {
        return (nodeExists(info) && !getNode(info).isNegated) || hasWildcard(info.id, info.guildId)
    }
}