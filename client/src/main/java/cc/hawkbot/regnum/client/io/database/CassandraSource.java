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

package cc.hawkbot.regnum.client.io.database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;

/**
 * Connector to the Cassandra database.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class CassandraSource {

    private static CassandraSource instance;

    /**
     * Returns the instance of the cassandra sources (needed for mapper).
     *
     * @return the instance of the cassandra sources
     */
    public static CassandraSource getInstance() {
        return instance;
    }

    private final Cluster cluster;
    private final CodecRegistry codecRegistry;
    private final String keyspace;
    private Session session;
    private MappingManager mappingManager;

    /**
     * Constructs a new CassandraSource
     *
     * @param username      the username
     * @param password      the password
     * @param database      the keyspace
     * @param codecRegistry the codec registry
     * @param contactPoints the contact points
     */
    public CassandraSource(@NotNull String username, @NotNull String password, @NotNull String database, @Nullable CodecRegistry codecRegistry, @NotNull String... contactPoints) {
        this.codecRegistry = Objects.requireNonNullElseGet(codecRegistry, CodecRegistry::new);
        cluster = Cluster.builder()
                .addContactPoints(contactPoints)
                .withAuthProvider(new PlainTextAuthProvider(username, password))
                .withCodecRegistry(codecRegistry)
                .build();
        this.keyspace = database;
        instance = this;
    }

    /**
     * Constructs a new CassandraSource
     *
     * @param username      the username
     * @param password      the password
     * @param database      the keyspace
     * @param contactPoints the contact points
     */
    public CassandraSource(String username, String password, String database, String... contactPoints) {
        this(username, password, database, null, contactPoints);
    }

    /**
     * Constructs a new CassandraSource
     *
     * @param username      the username
     * @param password      the password
     * @param database      the keyspace
     * @param codecRegistry the codec registry
     * @param contactPoints the contact points
     */
    public CassandraSource(String username, String password, String database, CodecRegistry codecRegistry, Collection<String> contactPoints) {
        this(username, password, database, codecRegistry, contactPoints.toArray(String[]::new));
    }

    /**
     * Constructs a new CassandraSource
     *
     * @param username      the username
     * @param password      the password
     * @param database      the keyspace
     * @param contactPoints the contact points
     */
    public CassandraSource(String username, String password, String database, Collection<String> contactPoints) {
        this(username, password, database, null, contactPoints);
    }

    /**
     * Connects to the Cassandra server.
     *
     * @return A CompletionStage containing the CassandraSource instance which completes when the connection got established
     */
    @SuppressWarnings("UnstableApiUsage")
    public CompletionStage<CassandraSource> connectAsync() {
        var future = new CompletableFuture<CassandraSource>();
        Futures.addCallback(cluster.connectAsync(keyspace), new FutureCallback<>() {
            @Override
            public void onSuccess(Session result) {
                session = result;
                mappingManager = new MappingManager(session);
                future.complete(CassandraSource.this);
            }

            @Override
            public void onFailure(@NotNull Throwable t) {
                future.completeExceptionally(t);
            }
        }, Executors.newSingleThreadExecutor(new DefaultThreadFactory("CassandraConnector")));
        return future;
    }

    /**
     * Connects to the Cassandra server and blocks the thread till the connection is established
     *
     * @return the source
     */
    public CassandraSource connect() {
        return connectAsync().toCompletableFuture().join();
    }

    /**
     * Returns the Cassandra {@link Cluster}.
     *
     * @return the Cassandra {@link Cluster}
     */
    public Cluster getCluster() {
        return cluster;
    }

    /**
     * Returns the {@link CodecRegistry} for object mapping.
     *
     * @return the {@link CodecRegistry} for object mapping
     */
    public CodecRegistry getCodecRegistry() {
        return codecRegistry;
    }

    /**
     * Returns the keyspace the client will use.
     *
     * @return the keyspace the client will use
     */
    public String getKeyspace() {
        return keyspace;
    }

    /**
     * Returns the current session {@link Session}.
     *
     * @return the current session
     */
    public Session getSession() {
        return session;
    }

    /**
     * Returns the object mapper {@link MappingManager}.
     *
     * @return the object mapper {@link MappingManager}
     */
    public MappingManager getMappingManager() {
        return mappingManager;
    }
}
