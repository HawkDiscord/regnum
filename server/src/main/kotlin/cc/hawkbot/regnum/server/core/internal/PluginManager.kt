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

package cc.hawkbot.regnum.server.core.internal

import cc.hawkbot.regnum.server.plugin.RegnumPlugin
import cc.hawkbot.regnum.server.plugin.Server
import cc.hawkbot.regnum.server.plugin.io.config.Config
import cc.hawkbot.regnum.util.logging.Logger
import de.foryasee.plugins.loader.impl.PluginLoader
import java.io.Closeable
import java.io.File

/**
 * Plugin manager.
 * @property regnum the [Server] instance
 * @constructor Constructs a new server
 */
class PluginManager(private val regnum: Server): Closeable {

    private val log = Logger.getLogger()
    private val pluginLoader = PluginLoader()
    private val plugins = mutableListOf<RegnumPlugin>()

    init {
        val pluginsFolder = File(regnum.config.get<String>(Config.PLUGINS_DIRECTORY))
        if (!pluginsFolder.exists()) {
            log.warn("[PluginLoader] Could not find plugins folder. Creating it ... Skipping loading of plugins!")
            pluginsFolder.mkdirs()

        }
        pluginsFolder.listFiles().filter { it.isFile && it.name.endsWith(".jar") }.forEach {
            val plugin = pluginLoader.load<RegnumPlugin>(it).join()
            // Inject server
            if (!plugin.succeeded()) {
                log.error("[PluginLoader] Could not load plugin", plugin.throwable)
                return@forEach
            }
            val regnumPlugin = plugin.plugin
            regnumPlugin.injectServer(regnum)
            regnumPlugin.onEnable()
            plugins.add(regnumPlugin)
        }
    }

    override fun close() {
        plugins.forEach {
            it.onDisable()
        }
    }
}