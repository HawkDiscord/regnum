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

package cc.hawkbot.regnum.server.plugin.events.websocket;

import cc.hawkbot.regnum.server.plugin.Server;
import cc.hawkbot.regnum.server.plugin.Websocket;
import io.javalin.websocket.WsSession;

@SuppressWarnings("unused")
public class WebSocketErrorEvent extends WebSocketSessionEvent {

    private final Throwable throwable;

    public WebSocketErrorEvent(Server server, Websocket websocket, WsSession session, Throwable throwable) {
        super(server, websocket, session);
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
