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

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.core.file.FileConfig
import java.io.Closeable
import java.io.File
import java.nio.file.Path


/**
 * Generic config yml config loader based on [FileConfig].
 */
@Suppress("unused")
open class GenericConfig(private val config: FileConfig) : Config, Closeable {


    /**
     * Creates a new config from the specified [source]
     */
    constructor(source: String): this(FileConfig.of(source))

    /**
     * Creates a new config from the specified [file]
     */
    constructor(file: File): this(FileConfig.of(file))

    /**
     * Creates a new config from the specified [path]
     */
    constructor(path: Path): this(FileConfig.of(path))

    /**
     * Creates a new config from the specified [path] with the specified [format]
     */
    constructor(path: Path, format: ConfigFormat<*>): this(FileConfig.of(path, format))


    init {
        load()
    }


    /*
     * Reads the config
     */
    @Suppress("MemberVisibilityCanBePrivate", "RedundantModalityModifier")
    final fun load() {
        val file = config.file
        if (!file.exists()) {
            val parent = file.parentFile
            if (!parent.exists()) {
                parent.mkdirs()
            }
            file.createNewFile()
        }
        config.load()
        defaults()
        config.save()
    }

    fun reload() {
        config.load()
    }

    /*
     * Method to apply defaults
     */
    @Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
    protected open fun defaults() {

    }


    /*
     * Sets a default to the config if it doesn't exist.
     * @param path the path of the option
     * @param value the value
     */
    protected fun applyDefault(path: String, value: Any) {
        if (!super.contains(path))
            super.add(path, value)
    }

    // See Config class for more information abot those methods

    override fun clear() {
        config.clear()
    }

    override fun <T : Any?> getRaw(path: MutableList<String>?): T {
        return config.getRaw(path)
    }

    override fun createSubConfig(): Config {
        return config.createSubConfig()
    }

    override fun add(path: MutableList<String>?, value: Any?): Boolean {
        return config.add(path, value)
    }

    override fun size(): Int {
        return config.size()
    }

    override fun entrySet(): MutableSet<out Config.Entry> {
        return config.entrySet()
    }

    override fun <T : Any?> remove(path: MutableList<String>?): T {
        return config.remove(path)
    }

    override fun <T : Any?> set(path: MutableList<String>?, value: Any?): T {
        return config.set(path, value)
    }

    override fun valueMap(): MutableMap<String, Any> {
        return config.valueMap()
    }

    override fun configFormat(): ConfigFormat<*> {
        return config.configFormat()
    }

    override fun contains(path: MutableList<String>?): Boolean {
        return config.contains(path)
    }

    override fun close() {
        config.close()
    }
}