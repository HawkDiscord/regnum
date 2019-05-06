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

package cc.hawkbot.regnum.client.core.discord

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.core.internal.RegnumImpl
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

/**
 * Repeating task that changes the bot presence every x seconds
 * @property regnum the Regnum instance
 * @constructor Constructs a new GameAnimator
 */
@Suppress("UNSAFE_CAST")
class GameAnimator(
        val regnum: Regnum
) {
    private val config = (regnum as RegnumImpl).gameAnimatorConfig
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private val shardManager
        get() = regnum.discord.shardManager
    private val games = config.games
    /**
     * The translator that is used to translate variables
     * @see cc.hawkbot.regnum.client.config.GameAnimatorConfig.translator
     */
    val translator = config.translator
    private val interval = config.interval

    /**
     * Starts the game animator
     */
    fun start() = scheduler.scheduleAtFixedRate(this::change, 0, interval, TimeUnit.SECONDS).run { Unit }

    private fun change() = games[ThreadLocalRandom.current().nextInt(games.size)].apply(this)

    /**
     * Class that represents a game for the game animator
     * @property type the activity type of the game. [**REFERENCE**](https://discordapp.com/developers/docs/topics/gateway#activity-object-activity-types)
     * @property status the OnlineStatus that will be activated when the game is shown. [**REFERENCE**](https://discordapp.com/developers/docs/topics/gateway#update-status)
     * @property content the text that is displayed in the Discord client
     */
    @Suppress("unused")
    class Game(
            val type: Int,
            val status: String,
            val content: String
    ) {

        /**
         * Apply a game to with the game animator
         * @param gameAnimator the game animator instance
         */
        fun apply(gameAnimator: GameAnimator) = gameAnimator.shardManager.applyGame(this)
    }
}