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

package cc.hawkbot.regnum.client.events.discord;

import cc.hawkbot.regnum.client.Regnum;
import cc.hawkbot.regnum.client.core.discord.ShardManager;
import cc.hawkbot.regnum.client.events.Event;

/**
 * Event that is fired when all Discord shards are connected and received READY event.
 */
public class ReadyEvent extends Event {

    private final int availableGuilds;
    private final int unavailableGuilds;

    /**
     * Constructs a new ready event.
     *
     * @param shardManager      the shard manager
     * @param availableGuilds   the count of available guilds
     * @param unavailableGuilds the count of unavailable guilds
     */
    @SuppressWarnings("unused")
    public ReadyEvent(Regnum regnum, ShardManager shardManager, int availableGuilds, int unavailableGuilds) {
        super(regnum);
        this.availableGuilds = availableGuilds;
        this.unavailableGuilds = unavailableGuilds;
    }


    /**
     * Returns the count of available guilds.
     *
     * @return the count of available guilds
     */
    public int getGuildAvailableCount() {
        return availableGuilds;
    }

    /**
     * Returns the count of unavailable guilds.
     *
     * @return the count of unavailable guilds
     */
    public int getGuildUnavailableCount() {
        return unavailableGuilds;
    }
}
