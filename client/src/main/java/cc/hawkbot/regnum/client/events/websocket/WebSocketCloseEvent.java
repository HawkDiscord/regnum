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

@SuppressWarnings("unused")
public class WebSocketCloseEvent extends WebSocketEvent {

    private final int code;
    private final String reason;
    private final boolean remote;

    public WebSocketCloseEvent(Regnum regnum, Websocket websocket, int code, String reason, boolean remote) {
        super(regnum, websocket);
        this.code = code;
        this.reason = reason;
        this.remote = remote;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public boolean isRemote() {
        return remote;
    }
}
