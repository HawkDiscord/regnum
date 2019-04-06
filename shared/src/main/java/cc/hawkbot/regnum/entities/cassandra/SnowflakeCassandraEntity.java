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

package cc.hawkbot.regnum.entities.cassandra;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.dv8tion.jda.api.entities.ISnowflake;

/**
 * Extension of {@link CassandraEntity} for snowflake entities.
 *
 * @see ISnowflake
 * @see CassandraEntity
 */
@SuppressWarnings({"unused", "CanBeFinal"})
public abstract class SnowflakeCassandraEntity<T> extends CassandraEntity<T> implements ISnowflake {

    @PartitionKey
    @Column(name = "id")
    @JsonIgnore
    private Long idAsLong;

    /**
     * Used for cache.
     *
     * @param idAsLong the id
     */
    public SnowflakeCassandraEntity(Long idAsLong) {
        this.idAsLong = idAsLong;
    }

    /**
     * Returns the id as a long.
     *
     * @return the id as a long
     */
    @Override
    @Transient
    public long getIdLong() {
        return idAsLong;
    }
}
