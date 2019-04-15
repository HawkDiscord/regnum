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

package cc.hawkbot.regnum.client.interaction;

import cc.hawkbot.regnum.client.Regnum;
import cc.hawkbot.regnum.client.util.SafeMessage;
import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.entities.DataMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public abstract class InteractableMessageBuilder<M extends InteractableMessage, B extends InteractableMessageBuilder<M, B>> {
    private final Regnum regnum;
    private Message message;
    private List<User> authorizedUsers = new ArrayList<>();
    private long timeout = 15;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public InteractableMessageBuilder(Regnum regnum) {
        this.regnum = regnum;
    }

    @NotNull
    public Regnum getRegnum() {
        return regnum;
    }

    @NotNull
    public Message getMessage() {
        return message;
    }

    public B  setMessage(@NotNull Message message) {
        if (message instanceof DataMessage) {
            throw new IllegalArgumentException("Cannot use DataMessage in InteractableMessages.");
        }
        this.message = message;
        return (B) this;
    }
    
    @NotNull
    public B setMessage(@NotNull TextChannel channel, @NotNull MessageBuilder builder) {
        var message = SafeMessage.sendMessage(builder, channel).complete(); 
        return setMessage(message);
    }

    @NotNull
    public B setMessage(@NotNull TextChannel channel, @NotNull String content) {
        return setMessage(channel, new MessageBuilder(content));
    }
    
    public B setMessage(TextChannel channel, MessageEmbed embed) {
        return setMessage(channel, new MessageBuilder(embed));
    }

    public B setMessage(TextChannel channel, EmbedBuilder embedBuilder) {
        return setMessage(channel, new MessageBuilder(embedBuilder));
    }

    public List<User> getAuthorizedUsers() {
        return authorizedUsers;
    }
    
    public B setAuthorizedUser(User user) {
        this.authorizedUsers = List.of(user);
        return (B) this;
    }

    public B setAuthorizedUsers(List<User> authorizedUsers) {
        authorizedUsers.addAll(this.authorizedUsers);
        this.authorizedUsers = authorizedUsers;
        return (B) this;
    }
    
    public B authorizeUsers(Collection<User> authorizedUsers) {
        this.authorizedUsers.addAll(authorizedUsers);
        return (B) this;
    }

    public B authorizeUsers(User... authorizedUsers) {
        Collections.addAll(this.authorizedUsers, authorizedUsers);
        return (B) this;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public B setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return (B) this;
    }
    
    protected void checks() {
        Preconditions.checkNotNull(message, "Message may not be null");
        Preconditions.checkState(!authorizedUsers.isEmpty(), "Authorized users may not be empty");
    }

    public abstract M build();
}
