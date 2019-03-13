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

package cc.hawkbot.regnum.entites.packets.discord;

import cc.hawkbot.regnum.entites.packets.Packet;

/**
 * Discord ADD packet.
 * Packet that is used to add another Discord shard to a node
 */
@SuppressWarnings("unused")
public class AddPacket implements Packet {

    /**
     * Type identifier
     */
    public static final String IDENTIFIER = "ADD_SHARD";

    private final Integer[] shards;

    /**
     * Constructs a new add packet
     *
     * @param shards the null-based shard ids
     */
    public AddPacket(Integer[] shards) {
        this.shards = shards;
    }

    /**
     * Returns the shard ids
     *
     * @return the shard ids
     */
    public Integer[] getShards() {
        return shards;
    }
}
