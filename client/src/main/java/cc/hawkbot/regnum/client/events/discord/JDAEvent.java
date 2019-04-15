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

package cc.hawkbot.regnum.client.events.discord;

import cc.hawkbot.regnum.client.Regnum;
import cc.hawkbot.regnum.client.core.discord.Discord;
import cc.hawkbot.regnum.client.events.Event;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

public class JDAEvent extends Event {

    private final JDA jda;

    /**
     * Constructs a new Regnum event.
     *
     * @param regnum the Regnum instance
     * @param jda    the jda shard instance on which the event got fired
     */
    public JDAEvent(@NotNull Regnum regnum, @NotNull JDA jda) {
        super(regnum);
        this.jda = jda;
    }

    @Override
    @NotNull
    public JDA getJDA() {
        return jda;
    }

    @NotNull
    public Discord getDiscord() {
        return getRegnum().getDiscord();
    }


}
