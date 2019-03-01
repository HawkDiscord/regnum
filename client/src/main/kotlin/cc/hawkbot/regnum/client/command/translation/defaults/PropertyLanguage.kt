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

package cc.hawkbot.regnum.client.command.translation.defaults

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.command.translation.Language
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.*

/**
 * Implementation of [Language] using [ResourceBundle].
 * @property locale the locale of the language
 * @param path the path to the [ResourceBundle]
 * @param encoding the charset of the [ResourceBundle]
 * @see Language
 * @constructor Constructs a new PropertyLanguage
 */
@Suppress("unused")
class PropertyLanguage(
        override val locale: Locale,
        path: String,
        encoding: Charset = Charsets.UTF_8
) : Language {

    private lateinit var regnum: Regnum

    private val resource = PropertyResourceBundle(InputStreamReader(FileInputStream(path), encoding))

    override fun get(key: String): String {
        return resource[key]
    }

    override fun has(key: String): Boolean {
        return resource.containsKey(key)
    }

    override fun regnum(): Regnum {
        return regnum
    }

    override fun regnum(regnum: Regnum) {
        this.regnum = regnum
    }
}

private operator fun ResourceBundle.get(key: String): String {
    return getString(key)
}
