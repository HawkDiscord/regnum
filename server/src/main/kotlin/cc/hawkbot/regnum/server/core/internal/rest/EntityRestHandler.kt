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

package cc.hawkbot.regnum.server.core.internal.rest

import cc.hawkbot.regnum.server.plugin.Server
import cc.hawkbot.regnum.server.plugin.rest.RestError
import cc.hawkbot.regnum.server.plugin.rest.RestHandler
import com.datastax.driver.mapping.Result
import io.javalin.BadRequestResponse
import io.javalin.Context
import io.javalin.core.HandlerType

/**
 * Rest handler for entities.
 */
abstract class EntityRestHandler<T>(endpoint: String, method: HandlerType, private val accessor: (id: Long, server: Server) -> Result<T>) : RestHandler(endpoint, method) {

    override fun handle(token: String?, context: Context, server: Server) {
        val id: Long
        try {
            id = context.validatedPathParam(":id").asLong().getOrThrow()
        } catch (e: BadRequestResponse) {
            context.status(400)
            context.json(RestError(
                    400,
                    "Bad request",
                    "Invalid id provided"
            ))
            return
        }
        if (server.restAuthorizationHandler.authorizeGuild(id, token, context)) {
            val entity = accessor(id, server)
            if (entity.availableWithoutFetching == 0) {
                context.status(404)
                context.json(RestError(
                        404,
                        "Not found",
                        "Entity not found"
                ))
                return
            }
            handle(entity.one(), token, context, server)
        }
    }

    /**
     * Function that gets invoked when the handler gets executed.
     * @param entity the entity
     * @param token the token if provided
     * @param context the context of the request
     * @param server the server instance
     */
    abstract fun handle(entity: T, token: String?, context: Context, server: Server)
}