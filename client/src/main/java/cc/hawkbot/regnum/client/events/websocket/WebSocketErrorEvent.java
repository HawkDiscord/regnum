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

/**
 * Event that is fired when an error with the websocket occurred.
 */
@SuppressWarnings("unused")
public class WebSocketErrorEvent extends WebSocketEvent {

    private final Throwable throwable;

    /**
     * Constructs a new websocket error event.
     *
     * @param regnum    the regnum instance
     * @param websocket the websocket instance
     * @param throwable the error
     * @see WebSocketEvent#WebSocketEvent(Regnum, Websocket)
     */
    public WebSocketErrorEvent(Regnum regnum, Websocket websocket, Throwable throwable) {
        super(regnum, websocket);
        this.throwable = throwable;
    }

    /**
     * Returns the error.
     *
     * @return the error.
     */
    public Throwable getThrowable() {
        return throwable;
    }
}
