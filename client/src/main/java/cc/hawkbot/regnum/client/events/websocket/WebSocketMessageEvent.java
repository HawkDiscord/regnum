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
import cc.hawkbot.regnum.entites.Payload;
import org.jetbrains.annotations.NotNull;

/**
 * Event that is fired when a websocket message got received.
 */
@SuppressWarnings("unused")
public class WebSocketMessageEvent extends WebSocketEvent {

    private final String message;

    /**
     * Constructs a new websocket message event.
     *
     * @param regnum    the regnum instance
     * @param websocket the websocket instance
     * @param message   the message
     * @see WebSocketEvent#WebSocketEvent(Regnum, Websocket)
     */
    public WebSocketMessageEvent(@NotNull Regnum regnum,@NotNull Websocket websocket,@NotNull String message) {
        super(regnum, websocket);
        this.message = message;
    }

    /**
     * Returns the message.
     *
     * @return the message
     */
    @NotNull
    public String getMessage() {
        return message;
    }

    @NotNull
    public Payload payload() {
        return Payload.fromJson(message);
    }
}
