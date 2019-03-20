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

package cc.hawkbot.regnum.client.config

import com.datastax.driver.core.CodecRegistry
import com.google.common.base.Preconditions
import java.util.*

/**
 * Configuration for Cassandra settings.
 * @property codecRegistry the [CodecRegistry]
 * @property keyspace the keyspace
 * @property username the username
 * @property password the password
 * @property contactPoints all contact points
 * @property defaultDatabases a list of cql queries which will be executed directly after connecting to cassandra
 * @constructor constructs a new Cassandra config
 */
data class CassandraConfig(
        var codecRegistry: CodecRegistry,
        var keyspace: String?,
        var username: String,
        var password: String?,
        var contactPoints: MutableList<String>,
        var defaultDatabases: MutableList<String> = mutableListOf()
) {

    /**
     * Configuration for Cassandra settings using the default [CodecRegistry].
     * @property keyspace the keyspace
     * @property username the username
     * @property password the password
     * @property contactPoints all contact points
     * @property defaultDatabases a list of cql queries which will be executed directly after connecting to cassandra
     * @constructor constructs a new Cassandra config
     */
    constructor(keyspace: String?, username: String, password: String?, contactPoints: MutableList<String>): this(CodecRegistry(), keyspace, username, password, contactPoints)

    /**
     * Configuration for Cassandra settings using the default [CodecRegistry] and no [defaultDatabases].
     * @property keyspace the keyspace
     * @property username the username
     * @property password the password
     * @property contactPoints all contact points
     * @constructor constructs a new Cassandra config
     */
    constructor(keyspace: String?, username: String, password: String?): this(keyspace, username, password, mutableListOf())

    /**
     * Configuration for Cassandra settings using the default [CodecRegistry], no keyspace and no [defaultDatabases].
     * @property username the username
     * @property password the password
     * @property contactPoints all contact points
     * @constructor constructs a new Cassandra config
     */
    constructor(username: String, password: String?): this(null, username, password)

    /**
     * Configuration for Cassandra settings using the default [CodecRegistry], no keyspace, no password and no [defaultDatabases].
     * @property username the username
     * @property password the password
     * @property contactPoints all contact points
     * @constructor constructs a new Cassandra config
     */
    constructor(username: String): this(username, null)

    /**
     * Configuration for Cassandra settings using the default [CodecRegistry], no keyspace, the Cassandra default login and no [defaultDatabases].
     * @property username the username
     * @property password the password
     * @property contactPoints all contact points
     * @constructor constructs a new Cassandra config
     */
    constructor(): this("cassandra", "cassandra")

    /**
     * Adds a Cassandra contact points.
     * @param addresses the address of the contact points
     * @return the builder.
     */
    fun addContactPoints(vararg addresses: String): CassandraConfig {
        Preconditions.checkNotNull<Any>(contactPoints, "contactPoints may not be null")
        Collections.addAll(contactPoints, *addresses)
        return this
    }

    /**
     * Adds a Cassandra contact points.
     * @param addresses the address of the contact points
     * @return the builder.
     */
    fun addContactPoints(addresses: Collection<String>): CassandraConfig {
        Preconditions.checkNotNull<Any>(contactPoints, "contactPoints may not be null")
        contactPoints.addAll(addresses)
        return this
    }

    /**
     * Adds a Cassandra default databases.
     * @param query the creation queries of the databases
     * @return the builder.
     */
    fun addDefaultDatabases(vararg query: String): CassandraConfig {
        Preconditions.checkNotNull<Any>(defaultDatabases, "defaultDatabases may not be null")
        Collections.addAll(defaultDatabases, *query)
        return this
    }

    /**
     * Adds a Cassandra default databases.
     * @param query the creation queries of the databases
     * @return the builder.
     */
    fun addDefaultDatabase(query: Collection<String>): CassandraConfig {
        Preconditions.checkNotNull<Any>(defaultDatabases, "defaultDatabases may not be null")
        defaultDatabases.addAll(query)
        return this
    }
}