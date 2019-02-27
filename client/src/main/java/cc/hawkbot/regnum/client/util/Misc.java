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

import cc.hawkbot.regnum.entites.Json;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Some useful utils.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Misc {

    public static final String HASTEBIN_URL = "https://paste.hawkbot.cc";

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
    public static <E, T> Optional<E> getKeyByValue(Map<E, T> map, T value) {
        return map.entrySet().stream().filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey).findFirst();
    }

    /**
     * Prints an exceptions stacktrace into a string.
     *
     * @param throwable the exception
     * @return the string
     */
    public static String stringifyException(Throwable throwable) {
        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.getBuffer().toString();
    }

    /**
     * Parses a string like 1s into it's timestamp.
     *
     * @param date The string
     * @return The date as an {@link java.time.OffsetDateTime}
     */
    public static OffsetDateTime parseDate(String date) {
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
    @SuppressWarnings("ConstantConditions")
    public static CompletableFuture<String> postToHastebinAsync(String message) {
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
    public static String postToHastebin(String message) {
        return postToHastebinAsync(message).join();
    }

}
