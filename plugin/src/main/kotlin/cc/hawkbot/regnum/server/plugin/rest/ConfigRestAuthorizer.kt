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
import cc.hawkbot.regnum.server.plugin.io.config.Config
import io.javalin.Context

/**
 * Implementation of [RestAuthorizationHandler] which compares the provided token with the token that is saved in the config.
 */
class ConfigRestAuthorizer : RestAuthorizationHandler {

    override lateinit var server: Server

    override fun authorizeGuild(id: Long, token: String?, context: Context): Boolean {
        if (!this::server.isInitialized) {
            context.status(502)
            context.json(RestError(
                    502,
                    "Internal server error",
                    "Server is not ready yet"
            ))
            return false
        }
        val auth = server.config.get<String>(Config.SOCKET_TOKEN)
        if (token != auth) {
            context.status(403)
            context.json(RestError(
                    401,
                    "Unauthorized",
                    "You provided an invalid token"
            ))
            return false
        }
        return true
    }
}