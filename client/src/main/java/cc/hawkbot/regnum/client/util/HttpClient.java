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

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Some useful HTTP methods.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class HttpClient {

    /**
     * The HTTP client {@link OkHttpClient} used by Regnum.
     */
    @SuppressWarnings("CanBeFinal")
    public static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

    /**
     * Executes a HTTP request asynchronously.
     *
     * @param request the request
     * @return a completable future containing the response {@link Response}
     */
    @NotNull
    public static CompletableFuture<Response> executeRequestAsync(@NotNull Request request) {
        var future = new CompletableFuture<Response>();
        OK_HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                future.complete(response);
            }
        });
        return future;
    }

    /**
     * Executes a HTTP Request synchronously.
     *
     * @param request the request
     * @return the response
     * @see HttpClient#executeRequestAsync(Request)
     */
    @NotNull
    public static Response executeRequest(@NotNull Request request) {
        return executeRequestAsync(request).join();
    }

    /**
     * Returns a request builder containing the specified url.
     *
     * @param url the url
     * @return the request builder
     */
    @NotNull
    public static Request.Builder url(@NotNull String url) {
        return new Request.Builder()
                .url(url);
    }

    /**
     * Returns a get request to the specified url.
     *
     * @param url the url
     * @return the request builder
     */
    @NotNull
    public static Request.Builder get(@NotNull String url) {
        return url(url)
                .get();
    }

    /**
     * Returns a post request to the specified url.
     *
     * @param url  the url
     * @param body the request boyd
     * @return the request builder
     */
    @NotNull
    public static Request.Builder post(@NotNull String url, @NotNull RequestBody body) {
        return url(url)
                .post(body);
    }

    /**
     * Returns a plain text body.
     *
     * @param content the content
     * @return the body
     */
    @NotNull
    public static RequestBody plainBody(@NotNull String content) {
        return RequestBody.create(null, content);
    }

    /**
     * Returns a json body.
     *
     * @param json the json
     * @return the body
     */
    @NotNull
    public static RequestBody jsonBody(@NotNull String json) {
        return RequestBody.create(MediaType.parse("application/json"), json);
    }
}
