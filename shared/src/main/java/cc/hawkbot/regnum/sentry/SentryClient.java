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

package cc.hawkbot.regnum.sentry;

import io.sentry.Sentry;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SentryClient {

    public static final String SENTRY_HOST = "sentry.io";

    private final io.sentry.SentryClient client;

    public SentryClient(String dsn) {
        client = Sentry.init(dsn);
    }

    public SentryClient(Protocol httpProtocol, String key, String host, String projectId) {
        this(String.format("%s://%s@%s/%s", httpProtocol.toString(), key, host, projectId));
    }

    public SentryClient(String key, String host, String projectId) {
        this(Protocol.HTTPS, key, host, projectId);
    }

    public SentryClient(Protocol httpProtocol, String key, String projectId) {
        this(httpProtocol, SENTRY_HOST, key, projectId);
    }

    public SentryClient(String key, String projectId) {
        this(Protocol.HTTPS, key, projectId);
    }

    public void sendEvent(Event event) {
        client.sendEvent(event);
    }

    public void sendEvent(EventBuilder eventBuilder) {
        client.sendEvent(eventBuilder);
    }

    public void sendMessage(String message) {
        client.sendMessage(message);
    }

    public void sendException(Throwable throwable) {
        client.sendException(throwable);
    }

    public io.sentry.SentryClient getClient() {
        return client;
    }

    public enum Protocol {
        HTTP("http"),
        HTTPS("https");

        private final String name;

        Protocol(String name) {
            this.name = name;
        }
    }
}
