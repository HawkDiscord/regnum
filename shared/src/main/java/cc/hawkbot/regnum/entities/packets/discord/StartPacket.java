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

package cc.hawkbot.regnum.entities.packets.discord;

import cc.hawkbot.regnum.entities.packets.Packet;

/**
 * Discord START packet.
 * Packet that is used to start Discord shards
 */
@SuppressWarnings("unused")
public class StartPacket implements Packet {

    /**
     * Type identifier
     */
    public static final String IDENTIFIER = "START";

    private String token;
    private Integer[] shards;
    private int shardsTotal;

    /**
     * Constructs a new StartPacket
     *
     * @param token       the Discord token
     * @param shards      the null-based shard ids
     * @param shardsTotal the total count of shards
     */
    public StartPacket(String token, Integer[] shards, int shardsTotal) {
        this.token = token;
        this.shards = shards;
        this.shardsTotal = shardsTotal;
    }

    /**
     * Used for serialization
     */
    public StartPacket() {

    }

    /**
     * Returns the Discord token
     *
     * @return the Discord token
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns the shard ids
     *
     * @return the shard ids
     */
    public Integer[] getShards() {
        return shards;
    }

    /**
     * Returns the total shard count
     *
     * @return the total shard count
     */
    public int getShardsTotal() {
        return shardsTotal;
    }
}
