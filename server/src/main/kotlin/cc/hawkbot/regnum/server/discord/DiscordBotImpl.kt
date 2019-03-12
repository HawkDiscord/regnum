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

package cc.hawkbot.regnum.server.discord

import cc.hawkbot.regnum.server.plugin.discord.DiscordBot
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.AnnotatedEventManager
import net.dv8tion.jda.api.hooks.SubscribeEvent

class DiscordBotImpl(token: String) : DiscordBot {

    override lateinit var jda: JDA

    init {
        val builder = JDABuilder(token)
                .setEventManager(AnnotatedEventManager())
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.playing("Starting ..."))
                .addEventListeners(this)
        jda = builder.build()
    }

    @SubscribeEvent
    @Suppress("unused")
    fun whenReady(event: ReadyEvent) {
        val presence = event.jda.presence
        presence.status = OnlineStatus.ONLINE
        presence.activity = Activity.playing("With my little brother Hawk")
    }

    override fun close() {
        jda.shutdownNow()
    }
}