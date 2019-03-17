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
import net.dv8tion.jda.api.exceptions.PermissionException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit

/**
 * Implementation of [PermissionManager]
 * @param regnum the current Regnum instance
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class PermissionManagerImpl(
        regnum: Regnum
) : PermissionManager {

    private val cassandra = regnum.cassandra
    private val accessor: PermissionNode.Accessor
    private val nullNode = PermissionNode(-1, false, -1, "null", PermissionNode.PermissionTarget.USER)
    private val cache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(object : CacheLoader<PermissionManager.PermissionInfoContainer, PermissionNode>() {
                override fun load(key: PermissionManager.PermissionInfoContainer): PermissionNode? {
                    val result = retrievePermissions(key)
                    if (result.availableWithoutFetching == 0)
                        return null
                    val node = result.one()
                    node.manager = this@PermissionManagerImpl
                    return node
                }
            })

    init {
        accessor = cassandra.mappingManager.createAccessor(PermissionNode.Accessor::class.java)
    }

    override fun hasPermissions(permissions: IPermissions, member: Member): Boolean {
        if (hasPermissions(permissions, permissionHolder = member)) {
            println("Memb $member")
            return true
        }
        member.roles.forEach {
            if (hasPermissions(permissions, it)) {
                println("role $it")
                return true
            }
        }
        return false
    }

    override fun hasPermissions(permissions: IPermissions, permissionHolder: IPermissionHolder): Boolean {
        val container = PermissionManager.PermissionInfoContainer(permissionHolder.idLong, permissionHolder.guild.idLong, permissions.node, permissions.public)
        return hasPermission(container)
    }

    override fun hasWildcard(id: Long, guildId: Long): Boolean {
        val info = PermissionManager.PermissionInfoContainer(id, guildId, "*", false)
        return nodeExists(info) && !getNode(info).isNegated
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
        } catch (e: CacheLoader.InvalidCacheLoadException) {
            false
        }
    }

    override fun updateNode(info: PermissionManager.PermissionInfoContainer, node: PermissionNode): CompletionStage<Void> {
        cache.put(info, node)
        return CompletableFuture.completedFuture(null)
    }

    override fun deleteNode(node: PermissionNode): CompletionStage<Void> {
        cache.invalidate(node.toInfo())
        return CompletableFuture.completedFuture(null)
    }

    override fun hasPermission(info: PermissionManager.PermissionInfoContainer): Boolean {
        if (nodeExists(info)) {
            val node = getNode(info)
            if (node.isNegated) {
                throw PermissionException("This permission is negated")
            } else {
                return true
            }
        }
        if (hasWildcard(info.id, info.guildId)) {
            return true
        }
        return info.public
    }
}