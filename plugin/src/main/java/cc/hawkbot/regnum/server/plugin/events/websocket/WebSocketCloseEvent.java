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
 * Event that indicates that a node closed it's connection.
 *
 * @see WebSocketSessionEvent
 */
@SuppressWarnings("unused")
public class WebSocketCloseEvent extends WebSocketSessionEvent {

    private final int code;
    private final String reason;
    private final Node node;

    public WebSocketCloseEvent(Server server, Websocket websocket, WsSession session, int code, String reason, Node node) {
        super(server, websocket, session);
        this.code = code;
        this.reason = reason;
        this.node = node;
    }

    /**
     * Returns the WebSocket close code.
     *
     * @return the WebSocket close code
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the reason for the disconnect.
     *
     * @return the reason for the disconnect
     */
    public String getReason() {
        return reason;
    }

    /**
     * Override because the node gets deleted after disconnects.
     *
     * @return the node
     * @see WebSocketSessionEvent#getNode()
     */
    @Override
    public Node getNode() {
        return node;
    }
}
