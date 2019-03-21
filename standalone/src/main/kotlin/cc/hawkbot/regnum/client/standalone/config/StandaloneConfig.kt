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

package cc.hawkbot.regnum.client.standalone.config

import cc.hawkbot.regnum.entites.json.JsonObject
import okhttp3.OkHttpClient
import okhttp3.Request

class StandaloneConfig(
        val token: String,
        val shardsTotal: Int,
        val shardIds: Array<Int>
) {

    companion object {
        fun auto(token: String): StandaloneConfig {
            val request = Request.Builder()
                    .addHeader("Authorization", token)
                    .url("https://discordapp.com/api/gateway/bot")
                    .build()
            OkHttpClient().newCall(request).execute().use {
                val body = it.body() ?: throw Exception("Could not retrieve shard count")
                val total = JsonObject(body.string()).getInt("shards")
                return StandaloneConfig(token, total)
            }
        }
    }

    constructor(token: String, shardsTotal: Int) : this(token, shardsTotal, (0 until (shardsTotal - 1)).toArray().toTypedArray())
}

private fun IntProgression.toArray() =
        IntArray(this.count()).also { forEachIndexed { index, i -> it[index] = i } }