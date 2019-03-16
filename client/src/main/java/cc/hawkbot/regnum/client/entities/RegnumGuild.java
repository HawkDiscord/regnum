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

package cc.hawkbot.regnum.client.entities;

import cc.hawkbot.regnum.client.entities.cache.CachableCassandraEntity;
import cc.hawkbot.regnum.client.entities.cassandra.CassandraEntity;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import org.jetbrains.annotations.NotNull;

import static cc.hawkbot.regnum.client.entities.cassandra.CassandraEntity.TABLE_PREFIX;

@Table(name = TABLE_PREFIX + "guilds")
@SuppressWarnings("unused")
public class RegnumGuild extends CachableCassandraEntity<RegnumGuild> {

    public static final String NO_PREFIX = "%NO%";

    public RegnumGuild() {
        super(0);
    }

    private String prefix = NO_PREFIX;

    public RegnumGuild(long id) {
        super(id);
    }

    @Transient
    public String getPrefix() {
        return prefix.equals(NO_PREFIX) ? regnum().getCommandParser().getDefaultPrefix() : prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @com.datastax.driver.mapping.annotations.Accessor
    public interface Accessor extends CachableCassandraEntity.Accessor<RegnumGuild> {
        @Query("SELECT * FROM " + TABLE_PREFIX + "guilds WHERE id = :id")
        @NotNull
        @Override
        Result<RegnumGuild> get(@Param("id") long id);
    }
}
