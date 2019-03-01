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

package cc.hawkbot.regnum.client.command.translation

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.command.translation.defaults.PropertyLanguage
import java.io.File
import java.nio.charset.Charset
import java.util.*

/**
 * Implementation of [LanguageManager] that will automatically load all languages in a specified folder.
 * @property path the path of the folder
 * @param defaultLanguage the default language
 * @property encoding the encoding
 * @constructor Constructs a new auto loading language manager
 */
@Suppress("unused")
class AutoLoadingLanguageManager(
        private val path: File,
        defaultLanguage: Language,
        private val encoding: Charset = Charsets.UTF_8
) : DefaultLanguageManager(defaultLanguage) {

    constructor(path: String, defaultLanguage: Language, encoding: Charset = Charsets.UTF_8): this(File(path), defaultLanguage, encoding)

    init {
        if (!path.exists()) {
            path.mkdirs()
        }
    }

    override fun regnum(regnum: Regnum) {
        super.regnum(regnum)
        loadLanguages()
    }

    private fun loadLanguages() {
        path.list().forEach {
            val file = File(it)
            if (!file.isFile || !file.exists()) {
                return@forEach
            }
            registerLanguage(PropertyLanguage(
                    Locale.forLanguageTag(file.name),
                    file.absolutePath,
                    encoding
            ))
        }
    }
}