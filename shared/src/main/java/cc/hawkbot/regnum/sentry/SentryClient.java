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

/**
 * A basic Sentry client.
 *
 * @see io.sentry.SentryClient
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class SentryClient {

    /**
     * The official sentry.io host.
     */
    public static final String SENTRY_HOST = "sentry.io";

    private final io.sentry.SentryClient client;

    /**
     * Constructs a new client using a Sentry dsn.
     *
     * @param dsn the dsn
     */
    public SentryClient(String dsn) {
        client = Sentry.init(dsn);
    }

    /**
     * Constructs a new client using Sentry credentials.
     *
     * @param key          the sentry key
     * @param httpProtocol the http protocol
     * @param host         the Sentry host
     * @param projectId    the Sentry project id
     */
    public SentryClient(Protocol httpProtocol, String key, String host, String projectId) {
        this(String.format("%s://%s@%s/%s", httpProtocol.getName(), key, host, projectId));
    }

    /**
     * Constructs a new client using Sentry credentials and HTTPS.
     *
     * @param key       the sentry key
     * @param host      the Sentry host
     * @param projectId the Sentry project id
     */
    public SentryClient(String key, String host, String projectId) {
        this(Protocol.HTTPS, key, host, projectId);
    }

    /**
     * Constructs a new client using Sentry credentials and sentry.io {@link SentryClient#SENTRY_HOST}.
     *
     * @param key          the sentry key
     * @param httpProtocol the http protocol
     * @param projectId    the Sentry project id
     */
    public SentryClient(Protocol httpProtocol, String key, String projectId) {
        this(httpProtocol, SENTRY_HOST, key, projectId);
    }

    /**
     * Constructs a new client using Sentry credentials and HTTPS and sentry.io {@link SentryClient#SENTRY_HOST}.
     *
     * @param key       the sentry key
     * @param projectId the Sentry project id
     */
    public SentryClient(String key, String projectId) {
        this(Protocol.HTTPS, key, projectId);
    }

    /**
     * Sends a built {@link Event} to the Sentry server.
     *
     * @param event event to send to Sentry.
     * @see io.sentry.SentryClient#sendEvent(Event)
     */
    public void sendEvent(Event event) {
        client.sendEvent(event);
    }

    /**
     * Builds and sends an {@link Event} to the Sentry server.
     *
     * @param eventBuilder {@link EventBuilder} to send to Sentry.
     * @see io.sentry.SentryClient#sendEvent(EventBuilder)
     */
    public void sendEvent(EventBuilder eventBuilder) {
        client.sendEvent(eventBuilder);
    }

    /**
     * Sends a message to the Sentry server.
     * <p>
     * The message will be logged at the {@link Event.Level#INFO} level.
     *
     * @param message message to send to Sentry.
     * @see io.sentry.SentryClient#sendMessage(String)
     */
    public void sendMessage(String message) {
        client.sendMessage(message);
    }

    /**
     * Sends an exception (or throwable) to the Sentry server.
     * <p>
     * The exception will be logged at the {@link Event.Level#ERROR} level.
     *
     * @see io.sentry.SentryClient#sendException(Throwable)
     * @param throwable exception to send to Sentry.
     */
    public void sendException(Throwable throwable) {
        client.sendException(throwable);
    }

    /**
     * Returns the actual {@link io.sentry.SentryClient}.
     * @return the actual {@link io.sentry.SentryClient}
     */
    public io.sentry.SentryClient getClient() {
        return client;
    }

    /**
     * Represents HTTPS or HTTP
     */
    public enum Protocol {
        HTTP("http"),
        HTTPS("https");

        private final String name;

        Protocol(String name) {
            this.name = name;
        }

        /**
         * Returns the name of the protocol.
         * @return the name of the protocol
         */
        public String getName() {
            return name;
        }}
}
