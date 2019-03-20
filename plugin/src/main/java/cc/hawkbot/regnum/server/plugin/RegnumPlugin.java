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

/**
 * Interface for Regnum plugins.
 */
@SuppressWarnings({"unused", "EmptyMethod"})
public abstract class RegnumPlugin implements Plugin {

    private Server server;

    /**
     * Method that is invoked when loading the plugin.
     */
    public void onEnable() {

    }

    /**
     * Method that is invoked when unloading the plugin.
     */
    public void onDisable() {

    }

    /**
     * Method that is used internally to inject the {@link Server} instance.
     * @param server the server
     */
    public void injectServer(Server server) {
        if (server == null) {
            throw new IllegalStateException("Server cannot be injected twice.");
        }
        this.server = server;
    }

    /**
     * Returns the server.
     * @return the {@link Server}
     */
    public Server getServer() {
        return server;
    }

    /**
     * Returns the websocket.
     * @return the {@link Websocket}
     */
    public Websocket getWebsocket() {
        return server.getWebsocket();
    }

    /**
     * Returns the config.
     * @return the {@link Config}
     */
    public Config getConfig() {
        return server.getConfig();
    }

    /**
     * Returns the Discord bot instance.
     * @return {@link DiscordBot}
     */
    public DiscordBot getDiscord() {
        return server.getDiscordBot();
    }
}
