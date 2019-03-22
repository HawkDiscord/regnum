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

package cc.hawkbot.regnum.server.entites;

import cc.hawkbot.regnum.entites.cassandra.SnowflakeCassandraEntity;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.hawkbot.regnum.entites.cassandra.CassandraEntity.TABLE_PREFIX;

/**
 * Entity for guilds.
 */
@Table(name = TABLE_PREFIX + "guilds")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@SuppressWarnings("unused")
public class Guild extends SnowflakeCassandraEntity<Guild> {

    public static final String NO_PREFIX = "%NO%";

    private String prefix = NO_PREFIX;
    @Column(name = "blacklisted_channels")
    private List<Long> blacklistedChannels;
    @Column(name = "whitelisted_channels")
    private List<Long> whitelistedChannels;
    @Column(name = "language_tag")
    private String languageTag = "en-US";

    public Guild() {
        super(0L);
    }

    public Guild(long id) {
        super(id);
    }

    @com.datastax.driver.mapping.annotations.Accessor
    public interface Accessor {
        @Query("SELECT * FROM " + TABLE_PREFIX + "guilds WHERE id = :id")
        @NotNull
        Result<Guild> get(@Param("id") long id);
    }
}
