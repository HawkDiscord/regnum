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

import cc.hawkbot.regnum.client.entities.RequiresRegnum
import net.dv8tion.jda.api.entities.ISnowflake
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User

/**
 * Manager for languages.
 * @property defaultLanguage the language that will be used if the users selected language does not contain the needed string.
 */
@Suppress("unused")
interface LanguageManager : RequiresRegnum {

    /**
     * The fallback language
     */
    val defaultLanguage: Language

    /**
     * A list of all languages.
     * @return a list of all languages.
     */
    fun languages(): Collection<Language>

    /**
     * Registers the [language]
     * @param language the language
     */
    fun registerLanguage(language: Language)

    /**
     * Registers the [languages]
     * @param languages the languages
     */
    fun registerLanguages(vararg languages: Language) {
        languages.forEach { registerLanguages(it) }
    }

    /**
     * Registers the [languages]
     * @param languages the language
     */
    fun registerLanguages(languages: Collection<Language>) {
        registerLanguages(*languages.toTypedArray())
    }

    /**
     * Returns the language by a users [id].
     * @return the language by a users [id]
     */
    fun getLanguageByUser(id: Long): Language

    /**
     * Returns the language by a users [id].
     * @return the language by a users [id]
     */
    fun getLanguageByUser(id: String): Language {
        return getLanguageByUser(id.toLong())
    }
    /**
     * Returns the language by a [snowflake]
     * @return the language by a [snowflake]
     */
    fun getLanguageByUser(snowflake: ISnowflake): Language {
        if (snowflake !is User && snowflake !is Member) {
            throw IllegalArgumentException("Only users and members can have a language")
        }
        return getLanguageByUser(snowflake.idLong)
    }

}