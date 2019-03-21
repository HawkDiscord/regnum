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

package cc.hawkbot.regnum.client.commands.settings

import cc.hawkbot.regnum.client.command.Command
import cc.hawkbot.regnum.client.command.Group
import cc.hawkbot.regnum.client.command.context.Arguments
import cc.hawkbot.regnum.client.command.context.Context
import cc.hawkbot.regnum.client.command.permission.CommandPermissions
import cc.hawkbot.regnum.client.util.EmbedUtil

/**
 * Default command for changing guild specific prefixes.
 * @constructor constructs the command
 */
class PrefixCommand : Command(Group.SETTINGS, "Prefix", arrayOf("prefix", "p"), CommandPermissions(node = "prefix", serverAdminExclusive = true), "[prefix]", "hw!", "Changes the prefix of your server!") {

    override fun execute(args: Arguments, context: Context) {
        val guild = context.regnumGuild()
        if (args.isEmpty()) {
            context.sendMessage(EmbedUtil.info(
                    context.translate("command.prefix.current.title"),
                    context.translate("command.prefix.current.description")
                            .format(guild.prefix)
            )).queue()
            return
        }
        val prefix = args[0]
        if (prefix.length > 3) {
            context.sendMessage(EmbedUtil.error(
                    context.translate("command.prefix.length.title"),
                    context.translate("command.prefix.length.description")
                            .format(3)
            )).queue()
            return
        }
        guild.prefix = prefix
        guild.saveAsync()
                .thenAccept {
                    context.sendMessage(EmbedUtil.success(
                            context.translate("command.prefix.success.title"),
                            context.translate("command.prefix.success.description")
                                    .format(guild.prefix)
                    )).queue()
                }
                .exceptionally { throw it }
    }
}