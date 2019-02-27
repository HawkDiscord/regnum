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

package cc.hawkbot.regnum.io.config

import org.simpleyaml.configuration.file.YamlFile

/**
 * Generic config yml config loader based on [YamlFile].
 */
open class GenericConfig(source: String) : YamlFile(source) {

    init {
        load()
    }

    /**
     * Reads the config
     */
    final override fun load() {
        createNewFile(false)
        super.load()
        defaults()
        super.save()
    }

    /**
     * Method to apply defaults
     */
    @Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
    protected open fun defaults() {

    }

    /**
     * Sets a default to the config if it doesn't exist.
     * @param path the path of the option
     * @param value the value
     */
    protected fun applyDefault(path: String, value: Any) {
        if (!super.isSet(path))
            super.set(path, value)
    }
}