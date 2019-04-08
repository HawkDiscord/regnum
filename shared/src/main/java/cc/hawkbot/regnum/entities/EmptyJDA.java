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

package cc.hawkbot.regnum.entities;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.DirectAudioController;
import net.dv8tion.jda.api.managers.Presence;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.GuildAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheView;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import okhttp3.OkHttpClient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class EmptyJDA {

    public static final JDA INSTANCE = new JDA() {
        @Override
        public Status getStatus() {
            return null;
        }

        @Override
        public long getGatewayPing() {
            return 0;
        }

        @Override
        public JDA awaitStatus(Status status) {
            return null;
        }

        @Override
        public List<String> getCloudflareRays() {
            return null;
        }

        @Override
        public List<String> getWebSocketTrace() {
            return null;
        }

        @Override
        public ScheduledExecutorService getRateLimitPool() {
            return null;
        }

        @Override
        public ScheduledExecutorService getGatewayPool() {
            return null;
        }

        @Override
        public ExecutorService getCallbackPool() {
            return null;
        }

        @Override
        public OkHttpClient getHttpClient() {
            return null;
        }

        @Override
        public DirectAudioController getDirectAudioController() {
            return null;
        }

        @Override
        public void addEventListener(Object... listeners) {

        }

        @Override
        public void removeEventListener(Object... listeners) {

        }

        @Override
        public List<Object> getRegisteredListeners() {
            return null;
        }

        @Override
        public GuildAction createGuild(String name) {
            return null;
        }

        @Override
        public CacheView<AudioManager> getAudioManagerCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<User> getUserCache() {
            return null;
        }

        @Override
        public List<Guild> getMutualGuilds(User... users) {
            return null;
        }

        @Override
        public List<Guild> getMutualGuilds(Collection<User> users) {
            return null;
        }

        @Override
        public RestAction<User> retrieveUserById(String id) {
            return null;
        }

        @Override
        public RestAction<User> retrieveUserById(long id) {
            return null;
        }

        @Override
        public SnowflakeCacheView<Guild> getGuildCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<Role> getRoleCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<Category> getCategoryCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<TextChannel> getTextChannelCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<PrivateChannel> getPrivateChannelCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<Emote> getEmoteCache() {
            return null;
        }

        @Override
        public IEventManager getEventManager() {
            return null;
        }

        @Override
        public void setEventManager(IEventManager manager) {

        }

        @Override
        public SelfUser getSelfUser() {
            return null;
        }

        @Override
        public Presence getPresence() {
            return null;
        }

        @Override
        public ShardInfo getShardInfo() {
            return null;
        }

        @Override
        public String getToken() {
            return null;
        }

        @Override
        public long getResponseTotal() {
            return 0;
        }

        @Override
        public int getMaxReconnectDelay() {
            return 0;
        }

        @Override
        public void setRequestTimeoutRetry(boolean retryOnTimeout) {

        }

        @Override
        public boolean isAutoReconnect() {
            return false;
        }

        @Override
        public void setAutoReconnect(boolean reconnect) {

        }

        @Override
        public boolean isAudioEnabled() {
            return false;
        }

        @Override
        public boolean isBulkDeleteSplittingEnabled() {
            return false;
        }

        @Override
        public void shutdown() {

        }

        @Override
        public void shutdownNow() {

        }

        @Override
        public AccountType getAccountType() {
            return null;
        }

        @Override
        public RestAction<ApplicationInfo> retrieveApplicationInfo() {
            return null;
        }

        @Override
        public String getInviteUrl(Permission... permissions) {
            return null;
        }

        @Override
        public String getInviteUrl(Collection<Permission> permissions) {
            return null;
        }

        @Override
        public ShardManager getShardManager() {
            return null;
        }

        @Override
        public RestAction<Webhook> retrieveWebhookById(String webhookId) {
            return null;
        }
    };
}
