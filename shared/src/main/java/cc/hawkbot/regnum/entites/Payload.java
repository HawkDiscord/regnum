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

package cc.hawkbot.regnum.entites;

import cc.hawkbot.regnum.entites.packets.Packet;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class Payload {

    public static Payload fromJson(String json) {
        return Json.fromJson(Payload.class, json);
    }

    public static Payload of(Packet packet, String identifier) {
        return new Payload(identifier, packet);
    }

    @JsonProperty("t")
    private String type;

    @JsonProperty("d")
    private Packet packet;

    public Payload(@Nonnull String type, @Nonnull Packet packet) {
        this.type = type;
        this.packet = packet;
    }

    /**
     * Used for serialization
     */
    public Payload() {

    }

    public String toJson() {
        return Json.toJson(this);
    }

    public String getType() {
        return type;
    }

    public Packet getPacket() {
        return packet;
    }
}
