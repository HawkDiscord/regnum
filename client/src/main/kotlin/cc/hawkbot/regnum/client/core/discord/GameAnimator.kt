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
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

/**
 * Repeating task that changes the bot presence every x seconds
 * @property regnum the Regnum instance
 * @constructor Constructs a new GameAnimator
 */
class GameAnimator(
        val regnum: Regnum
) {
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private val shardManager = regnum.discord.shardManager
    private val games = (regnum as RegnumImpl).games
    /**
     * The translator that is used to translate variables
     * @see cc.hawkbot.regnum.client.RegnumBuilder.gameTranslator
     */
    val translator = (regnum as RegnumImpl).gameTranslator
    private val interval = (regnum as RegnumImpl).gameAnimatorInterval

    /**
     * Starts the game animator
     */
    fun start() {
        scheduler.scheduleAtFixedRate(this::change, 0, interval, TimeUnit.SECONDS)
    }

    private fun change() {
        games[ThreadLocalRandom.current().nextInt(games.size)].apply(this)
    }

    /**
     * Class that represents a game for the game animator
     * @property type the [Activity.ActivityType] of the game
     * @property status the [OnlineStatus] that will be activated when the game is shown
     * @property content the text that is displayed in the Discord client
     */
    @Suppress("unused")
    class Game(
            private val type: Activity.ActivityType,
            private val status: OnlineStatus,
            private val content: String
    ) {
        companion object {

            /**
             * Compiles a game from a string in the following format TYPE:ACTIVITY:CONTENT e.g ONLINE:0:playing
             * @param game the string
             * @return the compiled game
             */
            fun compile(game: String): Game {
                val args = game.split(":")
                val type = Activity.ActivityType.fromKey(args[1].toInt())
                val status = OnlineStatus.fromKey(args[0])
                val content = args.subList(2, args.size).joinToString(separator = " ")
                return Game(type, status, content)
            }
        }

        /**
         * Apply a game to with the game animator
         * @param gameAnimator the game animator instance
         */
        fun apply(gameAnimator: GameAnimator) {
            gameAnimator.shardManager.setGame(toActivity(gameAnimator))
            gameAnimator.shardManager.setStatus(status)
        }

        private fun toActivity(gameAnimator: GameAnimator): Activity {
            val content = gameAnimator.translator.apply(content.replace("%servers%", gameAnimator.shardManager.guilds.size.toString()).replace("%users%", gameAnimator.shardManager.users.size.toString()))
            return Activity.of(type, content)
        }
    }
}