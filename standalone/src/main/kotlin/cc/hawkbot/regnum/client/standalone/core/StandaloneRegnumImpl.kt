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

package cc.hawkbot.regnum.client.standalone.core

import cc.hawkbot.regnum.client.config.ServerConfig
import cc.hawkbot.regnum.client.core.discord.impl.DiscordImpl
import cc.hawkbot.regnum.client.core.internal.RegnumImpl
import cc.hawkbot.regnum.client.core.internal.WebsocketImpl
import cc.hawkbot.regnum.client.standalone.StandaloneRegnumBuilder
import cc.hawkbot.regnum.client.standalone.config.StandaloneConfig
import cc.hawkbot.regnum.client.util._setRegnum
import net.dv8tion.jda.api.hooks.IEventManager

/**
 * Implementation of Regnum which does not connect to the websocket
 */
class StandaloneRegnumImpl(
        builder: StandaloneRegnumBuilder,
        eventManager: IEventManager
) : RegnumImpl(
        ServerConfig("-1", "-1"),
        eventManager,
        builder.gameAnimatorConfig,
        builder.commandConfig,
        builder.disabledFeatures
) {

    override var websocket: WebsocketImpl
        get() = throw UnsupportedOperationException("Standalone Regnum client does not support websocket")
        set(_) {}
    override val token: String
        get() = throw UnsupportedOperationException("Standalone Regnum client does not support websocket token")

    init {
        _setRegnum(this)
        events()
        cassandra(builder.cassandraConfig)
        caches()
        commandManager(builder.commandConfig)
        discord(builder.standaloneConfig)
    }

    private fun discord(standaloneConfig: StandaloneConfig) {
        discord = DiscordImpl(
                this,
                standaloneConfig.token,
                standaloneConfig.shardIds,
                standaloneConfig.shardsTotal
        )
    }
}