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

package cc.hawkbot.regnum.client.entities.cache

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.entities.RequiresRegnum
import cc.hawkbot.regnum.client.entities.cassandra.SnowflakeCassandraEntity
import com.datastax.driver.mapping.Result
import java.util.concurrent.CompletionStage

/**
 * Extension of [cc.hawkbot.regnum.client.entities.cassandra.CassandraEntity]
 * @see SnowflakeCassandraEntity
 * @see cc.hawkbot.regnum.client.entities.cassandra.CassandraEntity
 */
// That looks strange Kotlin
abstract class CachableCassandraEntity<T : CachableCassandraEntity<T>>(id: Long) : SnowflakeCassandraEntity<T>(id), RequiresRegnum {

    @Transient
    private lateinit var cache: CassandraCache<T>
    @Transient
    private lateinit var regnum: Regnum

    /**
     * Updates entity in cache.
     */
    @Suppress("UNCHECKED_CAST")
    fun update() {
        cache.update(this as T)
    }

    /**
     * Saves the entity.
     * @see cc.hawkbot.regnum.client.entities.cassandra.CassandraEntity.saveAsync
     * @return a future that completes when the request ist finished
     */
    override fun saveAsync(): CompletionStage<Void> {
        update()
        return super.saveAsync()
    }

    /**
     * Deletes the entity.
     * @see cc.hawkbot.regnum.client.entities.cassandra.CassandraEntity.deleteAsync
     * @return a future that completes when the request ist finished
     */
    @Suppress("UNCHECKED_CAST")
    override fun deleteAsync(): CompletionStage<Void> {
        cache.delete(idLong)
        return super.deleteAsync()
    }

    @com.datastax.driver.mapping.annotations.Accessor
    interface Accessor<T> {
        operator fun get(id: Long): Result<T>
    }

    internal fun cache(cache: CassandraCache<T>) {
        this.cache = cache
    }

    override fun regnum(): Regnum {
        return regnum
    }

    override fun regnum(regnum: Regnum) {
        this.regnum = regnum
    }
}