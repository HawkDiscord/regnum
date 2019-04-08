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

package cc.hawkbot.regnum.client.entities;

import cc.hawkbot.regnum.client.Regnum;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for entities that needs a {@link Regnum} instance.
 */
@SuppressWarnings("UnusedReturnValue")
public interface RequiresRegnum {

    /**
     * Method that returns the {@link Regnum instance}.
     *
     * @return the Regnum instance
     */
    @NotNull
    Regnum regnum();

    /**
     * Method that injects the {@link Regnum} instance.
     *
     * @param regnum the instance
     */
    void regnum(@NotNull Regnum regnum);
}
