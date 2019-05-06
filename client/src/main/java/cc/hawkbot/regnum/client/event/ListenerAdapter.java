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

package cc.hawkbot.regnum.client.event;

import cc.hawkbot.regnum.client.event.impl.EventListener;
import cc.hawkbot.regnum.client.events.Event;
import cc.hawkbot.regnum.client.events.discord.ReadyEvent;
import cc.hawkbot.regnum.client.events.websocket.*;
import org.jetbrains.annotations.NotNull;

/**
 * Continence class which adapts all possible events when using {@link cc.hawkbot.regnum.client.event.impl.InterfacedEventManager}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ListenerAdapter implements EventListener {

    @Override
    public final void onEvent(@NotNull Object event) {
        if (event instanceof Event) {
            onGenericEvent((Event) event);
            if (event instanceof ReadyEvent) {
                onReady((ReadyEvent) event);
            }
            if (event instanceof WebSocketEvent) {
                onWebsocketEvent((WebSocketEvent) event);
                if (event instanceof WebSocketConnectedEvent) {
                    onWebsocketOpen((WebSocketConnectedEvent) event);
                } else if (event instanceof WebSocketMessageEvent) {
                    onWebsocketMessage((WebSocketMessageEvent) event);
                } else if (event instanceof WebSocketErrorEvent) {
                    onWebsocketError((WebSocketErrorEvent) event);
                } else if (event instanceof WebSocketCloseEvent) {
                    onWebsocketClose((WebSocketCloseEvent) event);
                }
            }
        }
    }

    // General
    protected void onGenericEvent(Event event) {

    }

    // Discord
    protected void onReady(ReadyEvent event) {

    }

    // Websocket

    protected void onWebsocketEvent(WebSocketEvent event) {

    }

    protected void onWebsocketOpen(WebSocketConnectedEvent event) {

    }

    protected void onWebsocketMessage(WebSocketMessageEvent event) {

    }

    protected void onWebsocketError(WebSocketErrorEvent event) {

    }

    protected void onWebsocketClose(WebSocketCloseEvent event) {

    }
}
