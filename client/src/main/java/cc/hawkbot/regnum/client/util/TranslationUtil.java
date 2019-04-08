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

import cc.hawkbot.regnum.client.Regnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/**
 * Some translation utils.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class TranslationUtil {

    /**
     * Translates a translation string.
     *
     * @param regnum the current Regnum instance
     * @param key    the translation string key
     * @param user   the user
     * @return the translated string
     */
    @NotNull
    public static String translate(@NotNull Regnum regnum, @NotNull String key, @NotNull User user) {
        return translate(regnum, key, user.getIdLong());
    }

    /**
     * Translates a translation string.
     *
     * @param regnum the current Regnum instance
     * @param key    the translation string key
     * @param userId the id of the user
     * @return the translated string
     */
    @NotNull
    public static String translate(@NotNull Regnum regnum, @NotNull String key, @NotNull String userId) {
        return translate(regnum, key, Long.parseUnsignedLong(userId));
    }

    /**
     * Translates a translation string.
     *
     * @param regnum the current Regnum instance
     * @param key    the translation string key
     * @param userId the id of the user
     * @return the translated string
     */
    @NotNull
    public static String translate(@NotNull Regnum regnum, @NotNull String key, @NotNull Long userId) {
        return regnum.getLanguageManager().getLanguageByUser(userId).translate(key);
    }

    /**
     * Translates a translation string.
     *
     * @param regnum the current Regnum instance
     * @param key    the translation string key
     * @param guild  the guild
     * @return the translated string
     */
    @NotNull
    public static String translateByGuild(@NotNull Regnum regnum, @NotNull String key, @NotNull Guild guild) {
        return translateByGuild(regnum, key, guild.getIdLong());
    }

    /**
     * Translates a translation string.
     *
     * @param regnum  the current Regnum instance
     * @param key     the translation string key
     * @param guildId the if of the guild
     * @return the translated string
     */
    @NotNull
    public static String translateByGuild(@NotNull Regnum regnum, @NotNull String key, @NotNull String guildId) {
        return translateByGuild(regnum, key, Long.parseUnsignedLong(guildId));
    }

    /**
     * Translates a translation string.
     *
     * @param regnum  the current Regnum instance
     * @param key     the translation string key
     * @param guildId the if of the guild
     * @return the translated string
     */
    @NotNull
    public static String translateByGuild(@NotNull Regnum regnum, @NotNull String key, @NotNull Long guildId) {
        return regnum.getLanguageManager().getLanguageByGuild(guildId).translate(key);
    }
}
