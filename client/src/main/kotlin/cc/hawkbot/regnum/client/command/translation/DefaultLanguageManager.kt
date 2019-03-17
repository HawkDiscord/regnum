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

/**
 * Default implementation of [LanguageManager].
 * @param defaultLanguage the language that should be used as a fallback language
 */
open class DefaultLanguageManager(override val defaultLanguage: Language) : LanguageManager {

    private val languages = mutableMapOf<String, Language>()
    private lateinit var regnum: Regnum

    init {
        languages[defaultLanguage.languageTag()] = defaultLanguage
    }

    override fun languages(): Collection<Language> {
        return languages.values
    }

    override fun registerLanguage(language: Language) {
        language.regnum(regnum)
        languages[language.languageTag()] = language
    }

    override fun getLanguageByUser(id: Long): Language {
        return languages[regnum.user(id).languageTag]!!
    }

    override fun getLanguageByGuild(id: Long): Language {
        return languages[regnum.guild(id).languageTag]!!
    }

    override fun regnum(): Regnum {
        return regnum
    }

    override fun regnum(regnum: Regnum) {
        this.regnum = regnum
        defaultLanguage.regnum(regnum)
    }

    override fun isTranslated(languageTag: String): Boolean {
        return languages.containsKey(languageTag)
    }
}