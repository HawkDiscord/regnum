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
        /**
         * Converts the [language] into a default language.
         * @return the default language
         */
        @JvmStatic
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

    /**
     * The languages representative [Locale].
     */
    val locale: Locale

    /**
     * The language tag of [locale].
     */
    val languageTag: String
        get() = locale.toLanguageTag()

    /**
     * The display name of the [locale].
     */
    val displayName: String
        get() = locale.displayName

    /**
     * Returns the language-tag.
     * @return the language-tag.
     */
    @Deprecated("We're replacing fluent getters with Kotlin fields", ReplaceWith("languageTag"))
    fun languageTag(): String {
        return languageTag
    }

    /**
     * Returns the display-name.
     * @return the display-name.
     */
    @Deprecated("We're replacing fluent getters with Kotlin fields", ReplaceWith("displayName"))
    fun displayName(): String {
        return displayName
    }

    /**
     * Returns whether the language contains the [key] or not.
     * @param key the key
     * @return whether the language contains the key or not
     */
    fun has(key: String): Boolean

    /**
     * Returns he translated string for the specified [key].
     * @param key the key
     * @return the translated string for the specified [key]
     */
    fun get(key: String): String

    /**
     * Returns the translated string from this language or from the default language if it isn't translated yet.
     * @param key the key
     * @return the translated string
     */
    fun translate(key: String): String {
        if (!has(key))
            return regnum().languageManager.defaultLanguage.translate(key)
        return get(key)
    }
}