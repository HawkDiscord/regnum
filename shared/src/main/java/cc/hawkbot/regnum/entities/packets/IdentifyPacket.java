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

package cc.hawkbot.regnum.entities.packets;


/**
 * Websocket IDENTIFY packet.
 * Used to authenticate nodes after connecting.
 */
@SuppressWarnings("unused")
public class IdentifyPacket implements Packet {

    /**
     * Type identifier
     */
    public static final String IDENTIFIER = "IDENTIFY";

    private String token;

    /**
     * Constructs a IdentifyPacket
     *
     * @param token the authentication token
     */
    public IdentifyPacket(String token) {
        this.token = token;
    }

    /**
     * Used for serialization
     */
    @SuppressWarnings("WeakerAccess")
    public IdentifyPacket() {
    }

    /**
     * Returns the token for identification.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }
}
