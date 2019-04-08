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

package cc.hawkbot.regnum.client;

import cc.hawkbot.regnum.client.config.CassandraConfig;
import cc.hawkbot.regnum.client.config.CommandConfig;
import cc.hawkbot.regnum.client.config.GameAnimatorConfig;
import cc.hawkbot.regnum.client.config.ServerConfig;
import cc.hawkbot.regnum.client.core.MemoryMessageCache;
import cc.hawkbot.regnum.client.core.MessageCache;
import cc.hawkbot.regnum.client.core.discord.GameAnimator;
import cc.hawkbot.regnum.client.core.internal.RegnumImpl;
import cc.hawkbot.regnum.io.database.CassandraSource;
import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.IEventManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Builder for {@link Regnum} instances
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class RegnumBuilder {

    private ServerConfig serverConfig;
    private GameAnimatorConfig gameAnimatorConfig;
    private CommandConfig commandConfig;
    private IEventManager eventManager = new AnnotatedEventManager();
    private CassandraConfig cassandraConfig;
    private List<Feature> disabledFeatures = new ArrayList<>();
    private MessageCache messageCache = new MemoryMessageCache();


    /**
     * Returns the configuration used by {@link cc.hawkbot.regnum.client.core.Websocket} to connect to the Regnum server.
     *
     * @return the {@link ServerConfig}
     */
    @NotNull
    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    /**
     * Sets the configuration used by {@link cc.hawkbot.regnum.client.core.Websocket} to connect to the Regnum server.
     *
     * @param config the {@link ServerConfig}
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder setServerConfig(@NotNull ServerConfig config) {
        this.serverConfig = config;
        return this;
    }

    /**
     * Returns the configuration used by the {@link GameAnimator}.
     *
     * @return the {@link GameAnimatorConfig}
     */
    @NotNull
    public GameAnimatorConfig getGameAnimatorConfig() {
        return gameAnimatorConfig;
    }

    /**
     * Sets the configuration used by the {@link GameAnimator}.
     *
     * @param gameAnimatorConfig the {@link GameAnimatorConfig}
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder setGameAnimatorConfig(@NotNull GameAnimatorConfig gameAnimatorConfig) {
        this.gameAnimatorConfig = gameAnimatorConfig;
        return this;
    }

    /**
     * Returns the configuration used by the {@link cc.hawkbot.regnum.client.command.CommandParser} and other parts of
     * the commands system.
     *
     * @return the {@link CommandConfig}
     */
    @NotNull
    public CommandConfig getCommandConfig() {
        return commandConfig;
    }

    /**
     * Sets the configuration used by the {@link cc.hawkbot.regnum.client.command.CommandParser} and other parts of
     * the commands system.
     *
     * @param commandConfig the {@link CommandConfig}
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder setCommandConfig(@NotNull CommandConfig commandConfig) {
        this.commandConfig = commandConfig;
        return this;
    }

    /**
     * Returns the current event manager {@link IEventManager}.
     *
     * @return the current event manager {@link IEventManager}
     */
    @NotNull
    public IEventManager getEventManager() {
        return eventManager;
    }

    /**
     * Sets the event manager {@link IEventManager} that is used by {@link Regnum} and the {@link cc.hawkbot.regnum.client.core.discord.Discord}.
     *
     * @param eventManager the event manager
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder setEventManager(@NotNull IEventManager eventManager) {
        this.eventManager = eventManager;
        return this;
    }

    /**
     * Returns the current list of event listeners.
     *
     * @return the current list of event listeners
     */
    @NotNull
    public List<Object> getEventListeners() {
        return eventManager.getRegisteredListeners();
    }


    /**
     * Registers event listeners.
     *
     * @param listeners the listeners
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder registerEvents(@NotNull Object... listeners) {
        for (Object listener : listeners) {
            eventManager.register(listener);
        }
        return this;
    }

    /**
     * Registers event listeners.
     *
     * @param listeners the listeners
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder registerEvents(@NotNull Collection<Object> listeners) {
        return registerEvents(listeners.toArray());
    }

    /**
     * Returns the configuration used by {@link CassandraSource}.
     *
     * @return the {@link CassandraConfig}
     */
    @NotNull
    public CassandraConfig getCassandraConfig() {
        return cassandraConfig;
    }

    /**
     * Sets the configuration used by {@link CassandraSource}.
     *
     * @param cassandraConfig the {@link CassandraConfig}
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder setCassandraConfig(@NotNull CassandraConfig cassandraConfig) {
        this.cassandraConfig = cassandraConfig;
        return this;
    }

    /**
     * Returns a list of all disabled features.
     *
     * @return the list.
     */
    @NotNull
    public List<Feature> getDisabledFeatures() {
        return disabledFeatures;
    }

    /**
     * Sets the list of disabled features.
     *
     * @param disabledFeatures the disabled features
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder setDisabledFeatures(@NotNull List<Feature> disabledFeatures) {
        this.disabledFeatures = disabledFeatures;
        return this;
    }

    /**
     * Disables features.
     *
     * @param features an array of features {@link Feature} to disable
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder disableFeatures(@NotNull Feature... features) {
        Preconditions.checkNotNull(disabledFeatures, "DisabledFeatures may not be null");
        Collections.addAll(disabledFeatures, features);
        return this;
    }

    /**
     * Disables features.
     *
     * @param features a collection of features {@link Feature} to disable
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder disableFeatures(@NotNull Collection<Feature> features) {
        Preconditions.checkNotNull(disabledFeatures, "DisabledFeatures may not be null");
        disabledFeatures.addAll(features);
        return this;
    }

    /**
     * Returns the current message cache.
     *
     * @return the {@link MessageCache}
     */
    @NotNull
    public MessageCache getMessageCache() {
        return messageCache;
    }

    /**
     * Sets the message cache
     *
     * @param messageCache the {@link MessageCache}
     * @return the current builder
     */
    @NotNull
    public RegnumBuilder setMessageCache(@NotNull MessageCache messageCache) {
        this.messageCache = messageCache;
        return this;
    }

    /**
     * Build a new {@link Regnum} instance and connects to the server
     *
     * @return the instance
     * @throws NullPointerException When a required argument is null
     */
    @NotNull
    public Regnum build() {
        // Null checks
        Preconditions.checkNotNull(serverConfig, "ServerConfig may not be null");
        Preconditions.checkNotNull(gameAnimatorConfig, "GameAnimator config may not be null");
        Preconditions.checkNotNull(commandConfig, "CommandSettings may not be null");
        Preconditions.checkNotNull(cassandraConfig, "CassandraConfig may not be null");
        Preconditions.checkNotNull(disabledFeatures, "DisabledFeatures may not be null");

        // Build
        return new RegnumImpl(
                serverConfig,
                eventManager,
                gameAnimatorConfig,
                commandConfig,
                disabledFeatures,
                messageCache
        ).init(
                serverConfig,
                cassandraConfig,
                commandConfig
        );
    }
}
