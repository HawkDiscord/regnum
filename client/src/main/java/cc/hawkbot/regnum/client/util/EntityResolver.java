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

package cc.hawkbot.regnum.client.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cc.hawkbot.regnum.client.util.Misc.first;

/**
 * Helpful util to parse entities by their ids, mentions or names.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class EntityResolver {

    // https://regex101.com/r/jFT2mB/3
    public static final Pattern MENTION_REGEX = Pattern.compile("(<(#|&?@!?)(\\d+)>)");

    /**
     * Method that can resolve any Catnip entity by it's id, mention or name.
     *
     * @param identifier The identifier that needs to be resolved
     * @param resolver   A function that can get the entity
     * @param <T>        The type of the entity
     * @return The entity or {@code null} if it doesn't exists
     * @throws IllegalStateException     If the mention is invalid
     * @throws IndexOutOfBoundsException If the mention is invalid
     */
    @Nullable
    public static <T> T resolveEntity(
            @NotNull String identifier,
            @NotNull Function<Long, T> resolver,
            @NotNull Function<String, T> nameResolver
    ) {
        long id;
        if (Misc.isNumeric(identifier)) {
            id = Long.parseLong(identifier);
        } else {
            Matcher matcher = MENTION_REGEX.matcher(identifier);
            if (matcher.matches()) {
                id = Long.parseLong(matcher.group(3));
            } else {
                return nameResolver.apply(identifier);
            }
        }
        return resolver.apply(id);
    }

    /**
     * Resolves a User {@link net.dv8tion.jda.api.entities.User} by it's id, mention or name.
     *
     * @param jda        The current jda instance
     * @param identifier The identifier that needs to be parsed
     * @return The User
     */
    @Nullable
    public static User resolveUser(@NotNull JDA jda, @NotNull String identifier) {
        return resolveEntity(
                identifier,
                jda::getUserById,
                name -> first(jda.getUsersByName(name, true))
        );
    }

    /**
     * Resolves a Member {@link Member} by it's id, mention or name.
     *
     * @param jda        The current jda instance
     * @param guildId    The id of where guild the member is on
     * @param identifier The identifier that needs to be parsed
     * @return The Member
     */
    @Nullable
    public static Member resolveMember(@NotNull JDA jda, long guildId, @NotNull String identifier) {
        var guild = jda.getGuildById(guildId);
        return resolveEntity(
                identifier,
                guild::getMemberById,
                name -> first(guild.getMembersByName(name, true))
        );
    }

    /**
     * Resolves a Member {@link Member} by it's id, mention or name.
     *
     * @param jda        The current jda instance
     * @param guild      The guild the member is on
     * @param identifier The identifier that needs to be parsed
     * @return The Member
     */
    @Nullable
    public static Member resolveMember(@NotNull JDA jda, @NotNull Guild guild, @NotNull String identifier) {
        return resolveMember(jda, guild.getIdLong(), identifier);
    }

    /**
     * Resolves a TextChannel {@link TextChannel} by it's id, mention
     * or name.
     *
     * @param jda        The current jda instance
     * @param guildId    The id of the guild where the member is on
     * @param identifier The identifier that needs to be parsed
     * @return The TextChannel
     */
    @Nullable
    public static TextChannel resolveTextChannel(@NotNull JDA jda, long guildId, @NotNull String identifier) {
        var guild = jda.getGuildById(guildId);
        return resolveEntity(identifier,
                guild::getTextChannelById
                , name -> first(guild.getTextChannelsByName(name, true)));

    }

    /**
     * Resolves a TextChannel {@link TextChannel} by it's id, mention
     * or name.
     *
     * @param jda        The current jda instance
     * @param guild      The guild where the member is on
     * @param identifier The identifier that needs to be parsed
     * @return The TextChannel
     */
    @Nullable
    public static TextChannel resolveTextChannel(@NotNull JDA jda, @NotNull Guild guild, @NotNull String identifier) {
        return resolveTextChannel(jda, guild.getIdLong(), identifier);
    }

    /**
     * Resolves a Role {@link Role} by it's id, mention or name.
     *
     * @param jda        The current jda instance
     * @param guildId    The id of the guild where the member is on
     * @param identifier The identifier that needs to be parsed
     * @return The Role
     */
    @Nullable
    public static Role resolveRole(@NotNull JDA jda, long guildId, @NotNull String identifier) {
        var guild = jda.getGuildById(guildId);
        return resolveEntity(
                identifier,
                guild::getRoleById,
                name -> first(guild.getRolesByName(identifier, true))
        );
    }

    /**
     * Resolves a Role {@link Role} by it's id, mention or name.
     *
     * @param jda        The current jda instance
     * @param guild      The guild where the member is on
     * @param identifier The identifier that needs to be parsed
     * @return The Role
     */
    @Nullable
    public static Role resolveRole(@NotNull JDA jda, @NotNull Guild guild, @NotNull String identifier) {
        return resolveRole(jda, guild.getIdLong(), identifier);
    }
}