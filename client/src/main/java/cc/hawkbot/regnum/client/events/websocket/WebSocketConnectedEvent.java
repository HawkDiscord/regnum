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
import org.java_websocket.handshake.ServerHandshake;

/**
 * Event that is fired when a websocket connection got established.
 */
@SuppressWarnings("unused")
public class WebSocketConnectedEvent extends WebSocketEvent {

    private final ServerHandshake handshake;

    /**
     * Constructs a new websocket connected event.
     *
     * @param regnum    the regnum instance
     * @param websocket the websocket instance
     * @param handshake the handshake {@link ServerHandshake}
     * @see WebSocketEvent#WebSocketEvent(Regnum, Websocket)
     */
    public WebSocketConnectedEvent(Regnum regnum, Websocket websocket, ServerHandshake handshake) {
        super(regnum, websocket);
        this.handshake = handshake;
    }

    /**
     * Returns the handshake {@link ServerHandshake}.
     *
     * @return the handshake {@link ServerHandshake}
     */
    public ServerHandshake getHandshake() {
        return handshake;
    }
}
