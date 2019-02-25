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

import cc.hawkbot.regnum.client.core.discord.GameAnimator;
import cc.hawkbot.regnum.client.core.internal.RegnumImpl;
import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.IEventManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class RegnumBuilder {

    private String host;
    private String token;
    private Function<String, String> gameTranslator = string -> string;
    private long gameAnimatorInterval = 30;
    private IEventManager eventManager = new AnnotatedEventManager();
    private List<Object> eventListeners = new ArrayList<>();
    private List<GameAnimator.Game> games = new ArrayList<>();

    public String getHost() {
        return host;
    }

    public RegnumBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public IEventManager getEventManager() {
        return eventManager;
    }

    public RegnumBuilder setEventManager(@NotNull IEventManager eventManager) {
        this.eventManager = eventManager;
        return this;
    }

    public List<Object> getEventListeners() {
        return eventListeners;
    }

    public RegnumBuilder setEventListeners(List<Object> listeners) {
        this.eventListeners = listeners;
        return this;
    }

    public RegnumBuilder registerEvents(Object... listeners) {
        Collections.addAll(eventListeners, listeners);
        return this;
    }

    public RegnumBuilder registerEvents(Collection<Object> listeners) {
        eventListeners.addAll(listeners);
        return this;
    }

    public String getToken() {
        return token;
    }

    public RegnumBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public List<GameAnimator.Game> getGames() {
        return games;
    }

    public RegnumBuilder setGames(List<GameAnimator.Game> games) {
        this.games = games;
        return this;
    }

    public RegnumBuilder addGames(GameAnimator.Game... games) {
        Collections.addAll(this.games, games);
        return this;
    }

    public RegnumBuilder addGames(Collection<GameAnimator.Game> games) {
        this.games.addAll(games);
        return this;
    }

    public Function<String, String> getGameTranslator() {
        return gameTranslator;
    }

    public RegnumBuilder setGameTranslator(Function<String, String> gameTranslator) {
        this.gameTranslator = gameTranslator;
        return this;
    }

    public long getGameAnimatorInterval() {
        return gameAnimatorInterval;
    }

    public RegnumBuilder setGameAnimatorInterval(long gameAnimatorInterval) {
        this.gameAnimatorInterval = gameAnimatorInterval;
        return this;
    }

    public Regnum build() {
        // Null checks
        Preconditions.checkNotNull(host, "Host may not be null");
        Preconditions.checkNotNull(token, "Token may not be null");
        Preconditions.checkNotNull(eventListeners, "Event listeners may not be null");
        Preconditions.checkNotNull(games, "Games may not be null");
        Preconditions.checkNotNull(gameTranslator, "Game translator may not be null");

        // Register events
        eventListeners.forEach(eventManager::register);

        // Build
        return new RegnumImpl(
                host,
                eventManager,
                token,
                games,
                gameTranslator,
                gameAnimatorInterval
        );
    }
}
