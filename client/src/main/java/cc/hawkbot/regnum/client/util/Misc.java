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

import cc.hawkbot.regnum.entities.json.Json;
import com.google.common.base.Preconditions;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.Helpers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Some useful utils.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Misc {

    public static final String HASTEBIN_URL = "https://haste.hawkbot.cc";
    public static ExecutorService EXECUTOR = Executors.newCachedThreadPool(new DefaultThreadFactory("Futures"));

    /**
     * Checks whether a string is numeric or not.
     *
     * @param string the string
     * @return whether a string is numeric or not
     */
    public static boolean isNumeric(@NotNull String string) {
        return string.chars().allMatch(Character::isDigit);
    }

    /**
     * Returns an key of a map by its value.
     *
     * @param map   the map
     * @param value the value
     * @param <E>   the type of the keys
     * @param <T>   the type of the values
     * @return an Optional {@link java.util.Optional} containing the key
     * @see Stream#findFirst()
     * @see Stream#filter(Predicate)
     */
    @NotNull
    public static <E, T> Optional<E> getKeyByValue(@NotNull Map<E, T> map, @NotNull T value) {
        return map.entrySet().stream().filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey).findFirst();
    }

    /**
     * Returns an key of a map by its value.
     *
     * @param map   the map
     * @param value the value
     * @param <E>   the type of the keys
     * @param <T>   the type of the values
     * @return the found key or {@code null} if there was no key
     * @see Stream#findFirst()
     * @see Stream#filter(Predicate)
     */
    @Nullable
    public static <E, T> E getKeyOrNullByValue(@NotNull Map<E, T> map, @NotNull T value) {
        return map.entrySet().stream().filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey).findFirst().orElse(null);
    }

    /**
     * Prints an exceptions stacktrace into a string.
     *
     * @param throwable the exception
     * @return the string
     */
    @NotNull
    public static String stringifyException(@NotNull Throwable throwable) {
        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.getBuffer().toString();
    }

    /**
     * Parses a string like 1s into it's timestamp.
     *
     * @param date The string
     * @return The date as an {@link java.time.OffsetDateTime} or {@code null} if it couldn't parse the date
     */
    @Nullable
    public static OffsetDateTime parseDate(@NotNull String date) {
        int amount = parseInt(date);
        if (amount == 0) {
            return null;
        }
        OffsetDateTime time = OffsetDateTime.now();
        if (date.contains("d")) {
            time = time.plusDays(amount);
        } else if (date.contains("m")) {
            time = time.plusMinutes(amount);
        } else if (date.contains("y")) {
            time = time.plusYears(amount);
        } else if (date.contains("M")) {
            time = time.plusMonths(amount);
        } else if (date.contains("h")) {
            time = time.plusHours(amount);
        } else if (date.contains("s")) {
            time = time.plusSeconds(amount);
        } else {
            return null;
        }
        return time;
    }

    private static int parseInt(String integer) {
        try {
            return Integer
                    .parseInt(integer.replace("d", "").replace("m", "").replace("y", "").replace("M", "")
                            .replace("h", "").replace("s", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Returns the first element of a collection.
     *
     * @param collection The collection
     * @param <T>        The type of the collections content
     * @return The first element or {@code null} if the collection is empty
     */
    @Nullable
    public static <T> T first(Collection<T> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(0);
        }
        return collection.iterator().next();
    }

    /**
     * Posts a document to hastebin.
     *
     * @param message The content of the document
     * @return A future containing the url
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static CompletableFuture<String> postToHastebinAsync(@NotNull String message) {
        var body = HttpClient.plainBody(message);
        var request = HttpClient.post(HASTEBIN_URL + "/documents", body)
                .build();
        var future = new CompletableFuture<String>();
        HttpClient.executeRequestAsync(request)
                .thenAccept(it -> {
                    assert it.body() != null;
                    try {
                        future.complete(HASTEBIN_URL + "/" + Json.parseJson(it.body().string()).get("key").asText());
                    } catch (IOException | NullPointerException e) {
                        future.completeExceptionally(e);
                    }
                })
                .exceptionally(it -> {
                    future.completeExceptionally(it);
                    return null;
                });
        return future;
    }

    /**
     * Posts a document to hastebin.
     *
     * @param message The content of the document
     * @return the url of the document
     * @see Misc#postToHastebinAsync(String)
     */
    @NotNull
    public static String postToHastebin(@NotNull String message) {
        return postToHastebinAsync(message).join();
    }

    /**
     * Adds and custom or unicode emote to a message.
     *
     * @param emoji   the id of the custom emote or unicode of the normal emote
     * @param message the message to react on
     * @throws NullPointerException When there is no emote with the given id
     * @see Message#addReaction(Emote)
     * @return a restaction which adds the emote
     */
    @NotNull
    public static RestAction<Void> addReaction(String emoji, Message message) {
        if (Helpers.isNumeric(emoji)) {
            var emote = message.getJDA().getEmoteById(emoji);
            Preconditions.checkNotNull(emote, "Emote id may not be invalid");
            return message.addReaction(emote);
        }
        return message.addReaction(emoji);
    }

}
