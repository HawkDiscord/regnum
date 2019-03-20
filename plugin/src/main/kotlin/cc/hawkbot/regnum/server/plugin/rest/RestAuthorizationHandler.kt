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

package cc.hawkbot.regnum.server.plugin.rest

import cc.hawkbot.regnum.server.plugin.Server
import io.javalin.Context

/**
 * Authorizer for Rest requests.
 */
interface RestAuthorizationHandler {

    /**
     * The [Server] instance which will be injected during startup.
     */
    var server: Server

    /**
     * Method that verified that the [token] is allowed to access the guild with the [id].
     * @param context more context
     */
    fun authorizeGuild(id: Long, token: String?, context: Context): Boolean

}