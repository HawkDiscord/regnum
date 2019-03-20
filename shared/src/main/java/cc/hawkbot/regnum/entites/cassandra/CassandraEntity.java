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

package cc.hawkbot.regnum.entites.cassandra;

import cc.hawkbot.regnum.io.database.CassandraSource;
import cc.hawkbot.regnum.util.logging.Logger;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.annotations.Transient;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "WeakerAccess", "CanBeFinal"})
public abstract class CassandraEntity<T> {

    @Transient
    public static final String TABLE_PREFIX = "regnum_";

    @Transient
    private static final org.slf4j.Logger log = Logger.getLogger();

    @Transient
    private static final Map<Class<? extends CassandraEntity>, Mapper> mapperPool = new HashMap<>();

    @Transient
    public static MessagePassingQueue.Consumer<CassandraEntity> DEFAULT_SUCCESS_HANDLER = (entity) -> log.debug(
            "[Database] Successfully saved {} to cc.hawkbot.regnum.io.database", entity.toString());
    @Transient
    public static Consumer<Throwable> DEFAULT_ERROR_HANDLER = (throwable) -> log.error(
            "[Database] Could not modify entity in cc.hawkbot.regnum.io.database", throwable);
    @Transient
    public static ExecutorService executor = Executors.newCachedThreadPool(new DefaultThreadFactory("Database"));

    /**
     * Used for serialization.
     */
    public CassandraEntity() {

    }

    /**
     * Saves the current entity.
     *
     * @return a CompletionStage that completes when the request succeeded
     */
    @SuppressWarnings("unchecked")
    public CompletionStage<Void> saveAsync() {
        return execute(getMapper().saveAsync((T) this));
    }

    /**
     * Saves the current entity.
     *
     * @return a void
     */
    public Void save() {
        return saveAsync().toCompletableFuture().join();
    }


    /**
     * Deletes the current entity.
     *
     * @return a CompletionStage that completes when the request succeeded
     */
    @SuppressWarnings("unchecked")
    public CompletionStage<Void> deleteAsync() {
        return execute(getMapper().deleteAsync((T) this));
    }

    /**
     * Deletes the current entity.
     *
     * @return a void
     */
    public Void delete() {
        return deleteAsync().toCompletableFuture().join();
    }

    @SuppressWarnings("UnstableApiUsage")
    private <E> CompletionStage<E> execute(ListenableFuture<E> listenableFuture) {
        var future = new CompletableFuture<E>();
        future = future.exceptionally(ex -> {
            DEFAULT_ERROR_HANDLER.accept(ex);
            return null;
        })
                .thenApply(it -> {
                    DEFAULT_SUCCESS_HANDLER.accept(this);
                    return it;
                });
        // #NONSENSE
        var finalFuture = future;
        Futures.addCallback(listenableFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(@Nullable E result) {
                finalFuture.complete(result);
            }

            @Override
            public void onFailure(@NotNull Throwable t) {
                finalFuture.completeExceptionally(t);
            }
        }, executor);
        return finalFuture;
    }

    @SuppressWarnings("unchecked")
    private Mapper<T> getMapper() {
        return (Mapper<T>) mapperPool.computeIfAbsent(getClass(), (klass) -> {
            var mapper = CassandraSource.getInstance().getMappingManager().mapper(klass);
            mapper.setDefaultGetOptions(Mapper.Option.consistencyLevel(ConsistencyLevel.ALL));
            return mapper;
        });
    }


}