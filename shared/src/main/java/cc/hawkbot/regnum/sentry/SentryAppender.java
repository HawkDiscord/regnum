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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Standard LOG4J appender which posts errors to Sentry.
 *
 * @see SentryClient
 */
@Plugin(
        name = "SentryAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE
)
@SuppressWarnings({"unused", "WeakerAccess"})
public class SentryAppender extends AbstractAppender {

    private static SentryClient client;
    private final Level[] WHITELISTED_LEVELS = {Level.ERROR, Level.FATAL, Level.TRACE};

    protected SentryAppender(String name, Filter filter, final Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }

    /**
     * Method that injects the {@link SentryClient}.
     *
     * @param sentry the client
     */
    public static void injectSentry(SentryClient sentry) {
        client = sentry;
    }

    /**
     * Plugin factory method.
     *
     * @param name   the name of the appender
     * @param filter any filter for the appender
     * @param layout the layout for the appender
     * @return a new Sentry appender
     */
    @PluginFactory
    public static SentryAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout) {
        return new SentryAppender(name, filter, layout)
                ;
    }

    @Override
    public void append(LogEvent event) {
        if (client == null) {
            return;
        }
        if (event.getThrown() != null) {
            client.sendException(event.getThrown());
        }

        if (Arrays.asList(WHITELISTED_LEVELS).contains(event.getLevel()) && event.getThrown() == null) {
            client.sendMessage(event.getMessage().getFormattedMessage());
        }
    }
}
