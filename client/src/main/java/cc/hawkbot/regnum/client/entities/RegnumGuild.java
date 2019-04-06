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

import cc.hawkbot.regnum.client.entities.cache.CacheableCassandraEntity;
import cc.hawkbot.regnum.client.entities.cache.CassandraCache;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.*;
import com.google.common.collect.Lists;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.hawkbot.regnum.entities.cassandra.CassandraEntity.TABLE_PREFIX;

/**
 * Entity for guilds.
 */
@Table(name = TABLE_PREFIX + "guilds")
@SuppressWarnings({"unused", "WeakerAccess", "CanBeFinal"})
public class RegnumGuild extends CacheableCassandraEntity<RegnumGuild> {

    public static final String NO_PREFIX = "%NO%";

    private String prefix = NO_PREFIX;
    @Column(name = "blacklisted_channels")
    private List<Long> blacklistedChannels = Lists.newArrayList();
    @Column(name = "whitelisted_channels")
    private List<Long> whitelistedChannels = Lists.newArrayList();

    @Column(name = "language_tag")
    private String languageTag = "en-US";

    public RegnumGuild() {
        super(0);
    }

    @CassandraCache.Constructor
    public RegnumGuild(long id) {
        super(id);
    }

    /**
     * Returns the prefix of the guild.
     *
     * @return the prefix of the guild
     */
    @Transient
    @NotNull
    public String getPrefix() {
        return prefix.equals(NO_PREFIX) ? regnum().getCommandParser().getDefaultPrefix() : prefix;
    }

    /**
     * Sets the prefix of the guild.
     *
     * @param prefix the new prefix
     */
    public void setPrefix(@NotNull String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the language tag of the guild.
     *
     * @return the language tag
     */
    @NotNull
    public String getLanguageTag() {
        return languageTag;
    }

    /**
     * Sets the language tag of the guild
     *
     * @param languageTag the new language tag.
     */
    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }

    /**
     * Adds a channel to the blacklist.
     *
     * @param channelId the id of the channel
     */
    public void blockChannel(long channelId) {
        blacklistedChannels.add(channelId);
    }

    /**
     * Removes a channel from the blacklist.
     *
     * @param channelId the id of the channel
     */
    public void unBlockChannel(long channelId) {
        blacklistedChannels.remove(channelId);
    }

    /**
     * Adds a channel to the whitelist.
     *
     * @param channelId the id of the channel
     */
    public void whitelistChannel(long channelId) {
        whitelistedChannels.add(channelId);
    }

    /**
     * Removes a channel from the whitelist.
     *
     * @param channelId the id of the channel
     */
    public void unWhitelistChannel(long channelId) {
        whitelistedChannels.remove(channelId);
    }

    /**
     * Returns whether the guild uses a command whitelist or not.
     *
     * @return whether the guild uses a command whitelist or not
     */
    public boolean usesBlacklist() {
        return !blacklistedChannels.isEmpty();
    }

    /**
     * Returns whether the guild uses a command blacklist or not.
     *
     * @return whether the guild uses a command blacklist or not
     */
    public boolean usesWhitelist() {
        return !whitelistedChannels.isEmpty();
    }

    /**
     * Returns whether commands are allowed in a channel or not.
     *
     * @param channelId the id of the channel
     * @return whether commands are allowed in a channel or not
     */
    public boolean areCommandsAllowed(String channelId) {
        return areCommandsAllowed(Long.parseUnsignedLong(channelId));
    }

    /**
     * Returns whether commands are allowed in a channel or not.
     *
     * @param channel the channel
     * @return whether commands are allowed in a channel or not
     */
    public boolean areCommandsAllowed(TextChannel channel) {
        return areCommandsAllowed(channel.getIdLong());
    }

    /**
     * Returns whether commands are allowed in a channel or not.
     *
     * @param channelId the id of the channel
     * @return whether commands are allowed in a channel or not
     */
    public boolean areCommandsAllowed(Long channelId) {
        if (usesBlacklist())
            return !blacklistedChannels.contains(channelId);
        else if (usesWhitelist())
            return whitelistedChannels.contains(channelId);
        else
            return true;
    }

    /**
     * Returns a list of all blacklisted channels.
     *
     * @return the list
     */
    @NotNull
    public List<Long> getBlacklistedChannels() {
        return blacklistedChannels;
    }

    /**
     * Returns a list of all whitelisted channels.
     *
     * @return the list
     */
    @NotNull
    public List<Long> getWhitelistedChannels() {
        return whitelistedChannels;
    }

    /**
     * Internally used accessor
     */
    @com.datastax.driver.mapping.annotations.Accessor
    public interface Accessor extends CacheableCassandraEntity.Accessor<RegnumGuild> {
        @Query("SELECT * FROM " + TABLE_PREFIX + "guilds WHERE id = :id")
        @NotNull
        @Override
        Result<RegnumGuild> get(@Param("id") long id);
    }
}
