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
import org.jetbrains.annotations.NotNull;

/**
 * Event that is fired when the websocket closes.
 */
@SuppressWarnings("unused")
public class WebSocketCloseEvent extends WebSocketEvent {

    private final int code;
    private final String reason;
    private final boolean remote;

    /**
     * Constructs a new Websocket close event.
     *
     * @param regnum    the Regnum instance
     * @param websocket the websocket instance
     * @param code      the response code
     * @param reason    the close reason
     * @param remote    the remote thing
     * @see WebSocketEvent#WebSocketEvent(Regnum, Websocket)
     */
    public WebSocketCloseEvent(@NotNull Regnum regnum, @NotNull Websocket websocket, @NotNull int code, @NotNull String reason, @NotNull boolean remote) {
        super(regnum, websocket);
        this.code = code;
        this.reason = reason;
        this.remote = remote;
    }

    /**
     * Returns the close code.
     *
     * @return the close code
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the close reason.
     *
     * @return the close reason
     */
    @NotNull
    public String getReason() {
        return reason;
    }

    /**
     * Returns whether the close was forced remotely or not.
     *
     * @return whether the close was forced remotely or not
     */
    public boolean isRemote() {
        return remote;
    }
}
