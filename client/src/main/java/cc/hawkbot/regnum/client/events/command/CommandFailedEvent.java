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

package cc.hawkbot.regnum.client.events.command;

import cc.hawkbot.regnum.client.command.context.Context;
import org.jetbrains.annotations.NotNull;

/**
 * Event is fired when an error occurred while executing a command.
 * @see CommandEvent
 */
@SuppressWarnings("unused")
public class CommandFailedEvent extends CommandEvent {

    private final Throwable error;

    /**
     * Constructs a new Command event.
     * @param context the context of the command
     * @param error the throwable which was thrown
     */
    public CommandFailedEvent(
            @NotNull Context context,
            @NotNull Throwable error
    ) {
        super(context);
        this.error = error;
    }

    /**
     * Returns the error.
     * @return the {@link Throwable}
     */
    @NotNull
    public Throwable getError() {
        return error;
    }
}
