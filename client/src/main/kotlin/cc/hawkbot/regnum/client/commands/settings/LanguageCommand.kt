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
import cc.hawkbot.regnum.client.command.SubCommand
import cc.hawkbot.regnum.client.command.context.Arguments
import cc.hawkbot.regnum.client.command.context.Context
import cc.hawkbot.regnum.client.command.permission.CommandPermissions
import cc.hawkbot.regnum.client.command.translation.Language
import cc.hawkbot.regnum.client.command.translation.LanguageManager
import cc.hawkbot.regnum.client.util.EmbedUtil

class LanguageCommand : Command(Group.SETTINGS, "Language", arrayOf("language", "languages", "lang"), CommandPermissions(node = "language", public = true), "[language]", "en-US", "Lets you change the bots language") {

    init {
        registerSubCommand(GuildCommand())
    }

    override fun execute(args: Arguments, context: Context) {
        val target = context.regnumUser()
        if (args.isEmpty()) {
            return context.sendMessage(
                    EmbedUtil.info(
                            context.translate("command.language.list.title"),
                            context.translate("command.language.list.description")
                                    .format(target.languageTag)
                    ).addField(
                            context.translate("command.language.list.languages"),
                            formatLanguageList(context.regnum.languageManager),
                            false
                    )

            ).queue()
        }
        val tag = args[0]
        if (!context.regnum.languageManager.isTranslated(tag)) {
            return context.sendMessage(
                    EmbedUtil.error(
                            context.translate("command.language.notfound.title"),
                            context.translate("command.language.notfound.description")
                    )
            ).queue()
        }
        if (target.languageTag == tag) {
            return context.sendMessage(
                    EmbedUtil.error(
                            context.translate("command.language.same.title"),
                            context.translate("command.language.same.description")
                    )
            ).queue()
        }
        val language = context.regnum.languageManager.getLanguageByTag(tag)
        target.languageTag = language.languageTag()
        target.saveAsync().thenRun {
            context.sendMessage(
                    EmbedUtil.success(
                            context.translate("command.language.success.title"),
                            context.translate("command.language.success.description")
                                    .format(language.displayName())
                    )
            ).queue()
        }
    }

    private fun formatLanguageList(languageManager: LanguageManager): String {
        val builder = StringBuilder()
        languageManager.languages()
                .forEach {
                    builder.append(" - ").append(it.displayName()).append('(').append('`').append(it.languageTag()).append('`').append(')').appendln()
                }
        return builder.toString()
    }

    private class GuildCommand : SubCommand("Guild", arrayOf("guild"), CommandPermissions(serverAdminExclusive = true, node = "language.guild"), "[languageTag]", "en-US", "Changes the language for guild specific messages") {
        override fun execute(args: Arguments, context: Context) {
            val target = context.regnumGuild()
            if (args.isEmpty()) {
                return context.sendMessage(
                        EmbedUtil.info(
                                context.translate("command.language.guild.current.title"),
                                context.translate("command.language.guild.current.description")
                                        .format(context.regnum.languageManager.getLanguageByTag(target.languageTag).displayName())
                        )
                ).queue()
            }
            val tag = args[0]
            if (!context.regnum.languageManager.isTranslated(tag)) {
                return context.sendMessage(
                        EmbedUtil.error(
                                context.translate("command.language.notfound.title"),
                                context.translate("command.language.notfound.description")
                        )
                ).queue()
            }
            if (target.languageTag == tag) {
                return context.sendMessage(
                        EmbedUtil.error(
                                context.translate("command.language.guild.same.title"),
                                context.translate("command.language.guild.same.description")
                        )
                ).queue()
            }
            val language = context.regnum.languageManager.getLanguageByTag(tag)
            target.languageTag = language.languageTag()
            target.saveAsync().thenRun {
                context.sendMessage(
                        EmbedUtil.success(
                                context.translate("command.language.guild.success.title"),
                                context.translate("command.language.guild.success.description")
                                        .format(language.displayName())
                        )
                ).queue()
            }
        }
    }
}

private fun LanguageManager.getLanguageByTag(tag: String): Language {
    return this.languages().first { it.languageTag().equals(tag, true) }
}