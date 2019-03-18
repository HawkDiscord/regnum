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

package cc.hawkbot.regnum.client.config

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.core.discord.GameAnimator

@Suppress("unused")
data class GameAnimatorConfig(
        val translator: (regnum: Regnum, raw: String) -> String,
        val interval: Long,
        @Suppress("MemberVisibilityCanBePrivate") val games: MutableList<GameAnimator.Game>
) {
    companion object {
        const val DEFAULT_INTERVAL = 30L
    }

    constructor(translator: (regnum: Regnum, raw: String) -> String,
                games: MutableList<GameAnimator.Game>): this(translator, DEFAULT_INTERVAL, games)

    constructor(games: MutableList<GameAnimator.Game>): this({
        regnum, raw -> translate(regnum, raw)
    }, games)

    constructor(): this(mutableListOf())

    fun registerGames(vararg games: GameAnimator.Game) {
        this.games.addAll(games)
    }

    fun registerGames(games: Collection<GameAnimator.Game>) {
        this.games.addAll(games)
    }

}
private fun translate(regnum: Regnum, raw: String): String {
    val jda = regnum.discord.shardManager
    return raw.
            replace("%servers%", jda.guilds.size.toString())
            .replace("%users%", jda.users.size.toString())
}

