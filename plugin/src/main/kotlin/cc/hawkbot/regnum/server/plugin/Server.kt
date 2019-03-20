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

package cc.hawkbot.regnum.server.plugin

import cc.hawkbot.regnum.entites.packets.MetricsPacket
import cc.hawkbot.regnum.io.database.CassandraSource
import cc.hawkbot.regnum.sentry.SentryClient
import cc.hawkbot.regnum.server.plugin.core.AuthorizationHandler
import cc.hawkbot.regnum.server.plugin.core.LoadBalancer
import cc.hawkbot.regnum.server.plugin.discord.DiscordBot
import cc.hawkbot.regnum.server.plugin.io.config.Config
import cc.hawkbot.regnum.server.plugin.rest.RestAuthorizationHandler
import cc.hawkbot.regnum.server.plugin.rest.RestHandler
import cc.hawkbot.regnum.waiter.impl.EventWaiter
import io.javalin.Javalin
import net.dv8tion.jda.api.hooks.IEventManager
import okhttp3.OkHttpClient
import java.io.Closeable

/**
 * Representation of the Regnum server.
 */
interface Server: Closeable {

    /**
     * Millis at the time the server got started.
     */
    val launchedAt: Long

    /**
     * Whether the server is in dev mode or not.
     */
    val dev: Boolean

    /**
     * The javalin instance.
     */
    val javalin: Javalin

    /**
     * The websocket instance.
     */
    val websocket: Websocket

    /**
     * The config.
     */
    val config: Config

    /**
     * The built in discord bot.
     */
    val discordBot: DiscordBot

    /**
     * The event waiter.
     */
    val eventWaiter: EventWaiter

    /**
     * The event manager.
     */
    val eventManager: IEventManager

    /**
     * The authorization handler.
     */
    var authorizationHandler: AuthorizationHandler

    /**
     * The loadbalancer.
     */
    val loadBalancer: LoadBalancer

    /**
     * The HTTP client used for requests.
     */
    val httpClient: OkHttpClient

    /**
     * Sentry client.
     */
    val sentry: SentryClient

    /**
     * The cassandra client.
     */
    val cassandraSource: CassandraSource

    /**
     * Authorization handler for rest api.
     */
    var restAuthorizationHandler: RestAuthorizationHandler

    /**
     * The average metrics of all node.
     */
    var averageMetrics: MetricsPacket

    /**
     * Registers a new [RestHandler].
     * @param handler the handler
     */
    fun registerRestHandler(handler: RestHandler)
}