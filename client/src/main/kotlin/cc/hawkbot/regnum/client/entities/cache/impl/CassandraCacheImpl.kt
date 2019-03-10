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

package cc.hawkbot.regnum.client.entities.cache.impl

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.entities.cache.CachableCassandraEntity
import cc.hawkbot.regnum.client.entities.cache.CassandraCache
import cc.hawkbot.regnum.client.io.database.CassandraSource
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class CassandraCacheImpl<T : CachableCassandraEntity<T>>(
        val regnum: Regnum,
        val clazz: KClass<T>,
        accessorClazz: Class<out CachableCassandraEntity.Accessor<T>>
) : CassandraCache<T> {

    private val accessor = CassandraSource.getInstance().mappingManager.createAccessor(accessorClazz)
    private val cache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build<Long, T>(object : CacheLoader<Long, T>() {
                override fun load(key: Long): T {
                    val result = accessor[key]
                    val instance = if (result.availableWithoutFetching > 0)
                        result.one()
                    else {
                        val entity = clazz.constructors.first().call(key)
                        entity
                    }
                    instance.cache(this@CassandraCacheImpl)
                    instance.regnum(regnum)
                    instance.save()
                    return instance
                }

            })

    override fun get(id: Long): T {
        return cache[id]
    }

    override fun set(id: Long, entity: T) {
        cache.put(id, entity)
    }

    override fun delete(id: Long) {
        cache.invalidate(id)
    }
}