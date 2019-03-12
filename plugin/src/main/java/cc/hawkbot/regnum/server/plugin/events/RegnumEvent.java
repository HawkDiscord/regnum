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

package cc.hawkbot.regnum.server.plugin.events;

import cc.hawkbot.regnum.events.NoJDAEvent;
import cc.hawkbot.regnum.server.plugin.Server;
import net.dv8tion.jda.api.JDA;

@SuppressWarnings("WeakerAccess")
public class RegnumEvent extends NoJDAEvent {

    private final Server server;

    public RegnumEvent(Server server) {
        this.server = server;
    }


    @Override
    public JDA getJDA() {
        return server.getDiscordBot().getJda();
    }

    @Override
    public long getResponseNumber() {
        throw new UnsupportedOperationException("Regnum events does not support response numbers");
    }
}
