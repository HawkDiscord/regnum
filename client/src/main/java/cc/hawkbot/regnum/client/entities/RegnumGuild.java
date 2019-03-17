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
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.*;
import org.jetbrains.annotations.NotNull;

import static cc.hawkbot.regnum.client.entities.cassandra.CassandraEntity.TABLE_PREFIX;

/**
 * Entity for guilds.
 */
@Table(name = TABLE_PREFIX + "guilds")
@SuppressWarnings("unused")
public class RegnumGuild extends CachableCassandraEntity<RegnumGuild> {

    public static final String NO_PREFIX = "%NO%";

    public RegnumGuild() {
        super(0);
    }

    private String prefix = NO_PREFIX;

    @Column(name = "language_tag")
    private String languageTag = "en-US";

    public RegnumGuild(long id) {
        super(id);
    }

    /**
     * Returns the prefix of the guild.
     * @return the prefix of the guild
     */
    @Transient
    public String getPrefix() {
        return prefix.equals(NO_PREFIX) ? regnum().getCommandParser().getDefaultPrefix() : prefix;
    }

    /**
     * Sets the prefix of the guild.
     * @param prefix the new prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the language tag of the guild.
     * @return the language tag
     */
    public String getLanguageTag() {
        return languageTag;
    }

    /**
     * Sets the language tag of the guild
     * @param languageTag the new language tag.
     */
    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }

    @com.datastax.driver.mapping.annotations.Accessor
    public interface Accessor extends CachableCassandraEntity.Accessor<RegnumGuild> {
        @Query("SELECT * FROM " + TABLE_PREFIX + "guilds WHERE id = :id")
        @NotNull
        @Override
        Result<RegnumGuild> get(@Param("id") long id);
    }
}
