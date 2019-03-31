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

package cc.hawkbot.regnum.client.entities

import cc.hawkbot.regnum.client.command.permission.IPermissionHolder
import cc.hawkbot.regnum.client.command.permission.IPermissions
import cc.hawkbot.regnum.client.entities.cache.CachableCassandraEntity
import cc.hawkbot.regnum.client.entities.cache.CassandraCache
import cc.hawkbot.regnum.entites.cassandra.CassandraEntity
import cc.hawkbot.regnum.client.entities.permission.PermissionNode
import com.datastax.driver.mapping.Result
import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.Param
import com.datastax.driver.mapping.annotations.Query
import com.datastax.driver.mapping.annotations.Table
import java.util.*
import java.util.concurrent.CompletionStage

/**
 * Entity for users.
 */
@Suppress("unused")
@Table(name = CassandraEntity.TABLE_PREFIX + "user")
class RegnumUser : CachableCassandraEntity<RegnumUser>, IPermissionHolder {

    /**
     * The language tag.
     */
    @Column(name = "language_tag")
    var languageTag = "en-US"

    @CassandraCache.Constructor
    constructor(id: Long) : super(id)

    constructor() : this(-1)

    /**
     * Returns the [Locale] of the user
     * @return the [Locale]
     */
    fun locale(): Locale {
        return Locale.forLanguageTag(languageTag)
    }

    override fun hasPermission(permission: IPermissions, guildId: Long): Boolean {
        return regnum().permissionManager.hasPermission(idLong, guildId, permission.node, permission.public)
    }

    override fun assignPermission(permission: IPermissions, guildId: Long, negated: Boolean): CompletionStage<PermissionNode> {
        return regnum().permissionManager.createPermissionNode(idLong, guildId, permission.node, PermissionNode.PermissionTarget.USER, negated)
    }

    override fun deletePermissionAssignment(permission: IPermissions, guildId: Long): CompletionStage<Void> {
        return regnum().permissionManager.deleteNode(regnum().permissionManager.getNode(idLong, guildId, permission.node))
    }

    @com.datastax.driver.mapping.annotations.Accessor
    interface Accessor : CachableCassandraEntity.Accessor<RegnumUser> {
        @Query("SELECT * FROM " + CassandraEntity.TABLE_PREFIX + "user WHERE id = :id")
        override fun get(@Param("id") id: Long): Result<RegnumUser>
    }

}
