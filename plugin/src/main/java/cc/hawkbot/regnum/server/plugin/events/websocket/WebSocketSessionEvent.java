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
import cc.hawkbot.regnum.server.plugin.entities.Node;
import io.javalin.websocket.WsSession;

/**
 * Generic event for websocket events with sessions
 *
 * @see WebSocketEvent
 */
@SuppressWarnings("unused")
public class WebSocketSessionEvent extends WebSocketEvent {

    private final WsSession session;

    WebSocketSessionEvent(Server server, Websocket websocket, WsSession session) {
        super(server, websocket);
        this.session = session;
    }

    /**
     * Returns the websocket session.
     *
     * @return the websocket session
     */
    public WsSession getSession() {
        return session;
    }

    /**
     * Returns the {@link Node} corresponding to the session.
     *
     * @return the {@link Node} corresponding to the session
     */
    public Node getNode() {
        return getWebsocket().getNode(session);
    }
}
