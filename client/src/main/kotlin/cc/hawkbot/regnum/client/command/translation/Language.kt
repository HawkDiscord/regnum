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
import cc.hawkbot.regnum.client.entities.RequiresRegnum
import java.util.*

/**
 * Represents a translated language.
 * @property locale the [Locale] object for the language
 */
@Suppress("unused")
interface Language : RequiresRegnum {

    companion object {
        fun defaultLanguage(language: Language): Language {
            return object : Language {
                override val locale: Locale
                    get() = language.locale

                override fun translate(key: String): String {
                    if (!language.has(key)) {
                        return "No translation found for key $key"
                    }
                    return language.get(key)
                }

                override fun get(key: String): String {
                    return language.get(key)
                }

                override fun has(key: String): Boolean {
                    return language.has(key)
                }

                override fun regnum(): Regnum {
                    return language.regnum()
                }

                override fun regnum(regnum: Regnum) {
                    language.regnum(regnum)
                }

            }
        }
    }

    val locale: Locale

    fun languageTag(): String {
        return locale.toLanguageTag()
    }

    fun displayName(): String {
        return locale.displayName
    }

    fun has(key: String): Boolean

    fun get(key: String): String

    fun translate(key: String): String {
        if (!has(key))
            return regnum().languageManager.defaultLanguage.translate(key)
        return get(key)
    }
}