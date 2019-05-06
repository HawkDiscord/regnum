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

package cc.hawkbot.regnum.util.logging;

import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.HashMap;
import java.util.Map;

/**
 * LoggerFactory with fallback implementation
 */
@SuppressWarnings("CanBeFinal")
public class Logger {

    /**
     * Log level that is used by fallback logger
     */
    @SuppressWarnings("WeakerAccess")
    public static Level LOG_LEVEL = Level.INFO;
    private static boolean SLF4J_FOUND;
    private static Map<Class<?>, org.slf4j.Logger> LOGGERS = new HashMap<>();

    static {
        var temp = false;
        try {
            Class.forName("org.slf4j.impl.StaticLoggerBinder");
            temp = true;
        } catch (ClassNotFoundException e) {
            // Print error
            LoggerFactory.getLogger(Logger.class);
            getFallbackLogger(Logger.class).warn("[Logger] No SLF4J implementation found! Applying fallback logger." +
                    "This is not recommended for production usage!");
        }
        SLF4J_FOUND = temp;
    }

    public static org.slf4j.Logger getLogger() {
        return getLogger(StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass());
    }

    /**
     * Gets a logger instance for the class where the method got invoked
     *
     * @return the logger
     */
    @SuppressWarnings("UnusedReturnValue")
    public static org.slf4j.Logger getLogger(Class clazz) {
        if (SLF4J_FOUND)
            return LoggerFactory.getLogger(clazz);
        return getFallbackLogger(clazz);
    }

    private static org.slf4j.Logger getFallbackLogger(Class clazz) {
        return LOGGERS.computeIfAbsent(clazz, __ -> new SimpleLogger(clazz.getName(), LOG_LEVEL));
    }
}
