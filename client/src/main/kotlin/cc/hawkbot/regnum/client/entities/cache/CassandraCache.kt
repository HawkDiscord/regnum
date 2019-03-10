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

import net.dv8tion.jda.api.entities.ISnowflake

/**
 * A cache of [CachableCassandraEntity].
 */
interface CassandraCache<T: CachableCassandraEntity<T>> {

    /**
     * Returns an entity by its [id].
     * @return an entity by its [id]
     */
    operator fun get(id: Long): T

    /**
     * Returns an entity by its [id].
     * @return an entity by its [id]
     */
    operator fun get(id: String): T {
        return get(id.toLong())
    }

    /**
     * Returns an entity by its instance.
     * @return an entity by its instance
     */
    operator fun get(entity: ISnowflake): T {
        return get(entity.idLong)
    }

    /**
     * Updates an entities instance
     * @param id the entities id
     * @param entity the entity
     */
    operator fun set(id: Long, entity: T)

    /**
     * Deletes an entity by its id
     * @param id the id
     */
    fun delete(id: Long)
}