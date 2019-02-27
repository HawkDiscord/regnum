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

package cc.hawkbot.regnum.client.events.websocket;

import cc.hawkbot.regnum.client.Regnum;
import cc.hawkbot.regnum.client.core.Websocket;
import cc.hawkbot.regnum.client.events.Event;

/**
 * Generic event for {@link Websocket}.
 */
@SuppressWarnings("unused")
public class WebSocketEvent extends Event {

    private final Websocket websocket;

    /**
     * Constructs a new websocket event.
     *
     * @param regnum    the regnum instance
     * @param websocket the websocket instance
     * @see Event#Event(Regnum)
     */
    @SuppressWarnings("WeakerAccess")
    public WebSocketEvent(Regnum regnum, Websocket websocket) {
        super(regnum);
        this.websocket = websocket;
    }

    /**
     * Returns the {@link Websocket} instance
     *
     * @return the {@link Websocket} instance
     */
    public Websocket getWebsocket() {
        return websocket;
    }
}
