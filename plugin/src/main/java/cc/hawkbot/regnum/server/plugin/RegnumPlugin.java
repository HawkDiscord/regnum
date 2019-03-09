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

package cc.hawkbot.regnum.server.plugin;

import cc.hawkbot.regnum.server.plugin.discord.DiscordBot;
import cc.hawkbot.regnum.server.plugin.io.config.Config;
import de.foryasee.plugins.Plugin;

@SuppressWarnings("unused")
public abstract class RegnumPlugin implements Plugin {

    private Server server;

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void injectServer(Server server) {
        if (server == null) {
            throw new IllegalStateException("Server cannot be injected twice.");
        }
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    public Websocket getWebsocket() {
        return server.getWebsocket();
    }

    public Config getConfig() {
        return server.getConfig();
    }

    public DiscordBot getDiscord() {
        return server.getDiscordBot();
    }
}
