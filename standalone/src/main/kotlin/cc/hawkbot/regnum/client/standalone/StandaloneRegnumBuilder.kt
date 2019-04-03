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

package cc.hawkbot.regnum.client.standalone

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.RegnumBuilder
import cc.hawkbot.regnum.client.config.ServerConfig
import cc.hawkbot.regnum.client.standalone.config.StandaloneConfig
import cc.hawkbot.regnum.client.standalone.core.StandaloneRegnumImpl
import com.google.common.base.Preconditions

/**
 * Builder for standalone Regnum.
 * @constructor Constructs a [StandaloneRegnumBuilder]
 */
class StandaloneRegnumBuilder() : RegnumBuilder() {

    @Suppress("unused")
    constructor(old: RegnumBuilder) : this() {
        gameAnimatorConfig = old.gameAnimatorConfig
        commandConfig = old.commandConfig
        cassandraConfig = old.cassandraConfig
        disabledFeatures = old.disabledFeatures
    }

    lateinit var standaloneConfig: StandaloneConfig

    override fun getServerConfig(): ServerConfig {
        throw UnsupportedOperationException("Standalone does not support server config")
    }


    override fun setServerConfig(config: ServerConfig): RegnumBuilder {
        throw UnsupportedOperationException("Standalone does not support server config")
    }

    override fun build(): Regnum {
        // Null checks
        Preconditions.checkNotNull(gameAnimatorConfig, "GameAnimator config may not be null")
        Preconditions.checkNotNull(commandConfig, "CommandSettings may not be null")
        Preconditions.checkNotNull(cassandraConfig, "CassandraConfig may not be null")
        Preconditions.checkNotNull(disabledFeatures, "DisabledFeatures may not be null")
        Preconditions.checkNotNull(standaloneConfig, "StandaloneConfig may not be null")
        Preconditions.checkNotNull(eventManager, "EventManager may not be null")

        return StandaloneRegnumImpl(this, this.eventManager)
    }
}