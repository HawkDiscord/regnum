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

package cc.hawkbot.regnum.client.commands.general

import cc.hawkbot.regnum.client.command.Command
import cc.hawkbot.regnum.client.command.Group
import cc.hawkbot.regnum.client.command.context.Arguments
import cc.hawkbot.regnum.client.command.context.Context
import cc.hawkbot.regnum.client.command.groups
import cc.hawkbot.regnum.client.command.permission.CommandPermissions
import cc.hawkbot.regnum.client.util.EmbedUtil
import cc.hawkbot.regnum.client.util.FormatUtil
import net.dv8tion.jda.api.EmbedBuilder

class HelpCommand: Command(Group.GENERAL, "Help", arrayOf("help", "h"), CommandPermissions(node = "help", public = true), "[command]", "help", "Gives your more information about the bots commands") {

    override fun execute(args: Arguments, context: Context) {
        val commandParser = context.regnum.commandParser
        if (args.isEmpty()) {
            val embed = EmbedBuilder()
            groups.forEach {
                embed.addField(it.name, FormatUtil.generateCommandList(it, commandParser), false)
            }
            return context.sendMessage(embed).queue()
        }
        val command = args[0]
        if (!commandParser.commands().containsKey(command)) {
            return context.sendMessage(
                    EmbedUtil.error(
                            context.translate("command.help.invalid.title"),
                            context.translate("command.help.invalid.description")
                    )
            ).queue()
        }
        val commandObject = commandParser.commands().getValue(command)
        context.sendMessage(
                FormatUtil.formatCommand(commandObject, context.regnumGuild(), context.regnum)
        ).queue()
    }
}