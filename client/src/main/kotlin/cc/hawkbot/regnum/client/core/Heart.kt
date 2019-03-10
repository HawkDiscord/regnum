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

package cc.hawkbot.regnum.client.core

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

/**
 * The Heart(beater) of the client.
 * @property lastHeartbeat the timestamp of the last heartbeat
 * @property ping the last ping between the client and the server
 */
@Suppress("unused")
interface Heart {

    var lastHeartbeat: Long
    var ping: Int

    /**
     * Returns the heartbeat as an [OffsetDateTime].
     * @return the heartbeat as an [OffsetDateTime]
     */
    fun lastHeartbeat(): OffsetDateTime {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(lastHeartbeat), ZoneId.systemDefault())
    }

}