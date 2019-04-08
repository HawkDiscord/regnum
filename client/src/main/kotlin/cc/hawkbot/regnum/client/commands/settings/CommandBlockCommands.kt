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
import cc.hawkbot.regnum.client.entities.RegnumGuild
import cc.hawkbot.regnum.client.util.EmbedUtil
import net.dv8tion.jda.api.entities.TextChannel

/**
 * Generic command for black and whitelist.
 * @param displayName name of the command
 * @param aliases aliases of the command
 * @param description description of the command
 * @param listDescription description of the list sub command
 * @param addDescription description of the add sub command
 * @param removeDescription description of the remove sub command
 * @param permission permission prefix
 */
abstract class ChannelBlockCommand(
        displayName: String,
        aliases: Array<String>,
        description: String,
        listDescription: String,
        addDescription: String,
        removeDescription: String,
        permission: String
) : Command(
        Group.SETTINGS,
        displayName,
        aliases,
        CommandPermissions(node = "blacklist", serverAdminExclusive = true),
        "<add/remove/list> [#channel]",
        "add #talk",
        description
) {

    init {
        @Suppress("LeakingThis")
        registerSubCommands(
                ListCommand(listDescription, permission) { ctx -> list(ctx) },
                AddCommand(addDescription, permission, { ctx, channel -> add(ctx, channel) }, this),
                RemoveCommand(removeDescription, permission, { ctx, channel -> remove(ctx, channel) }, this)
        )
    }

    override fun execute(args: Arguments, context: Context) {
        if (args.isEmpty()) {
            return context.sendHelp().queue()
        }
    }

    private class ListCommand(listDescription: String, permission: String, private val action: (context: Context) -> Unit) : SubCommand("List", arrayOf("list", "ls", "all"), CommandPermissions(node = "$permission.list", public = true), "", "", listDescription) {
        override fun execute(args: Arguments, context: Context) {
            action(context)
        }
    }

    private class AddCommand(addDescription: String, permission: String, private val action: (context: Context, channel: TextChannel) -> Unit, private val superCommand: ChannelBlockCommand) : SubCommand("Add", arrayOf("add", "block"), CommandPermissions(node = "$permission.add", serverAdminExclusive = true), "<#TextChannel>", "#general", addDescription) {
        override fun execute(args: Arguments, context: Context) {
            superCommand.checkMention(context) { ctx, channel ->
                action(ctx, channel)
            }
        }
    }

    private class RemoveCommand(addDescription: String, permission: String, private val action: (context: Context, channel: TextChannel) -> Unit, private val superCommand: ChannelBlockCommand) : SubCommand("Remove", arrayOf("remove", "rm", "unblock"), CommandPermissions(node = "$permission.remove", serverAdminExclusive = true), "<#TextChannel>", "#general", addDescription) {
        override fun execute(args: Arguments, context: Context) {
            superCommand.checkMention(context) { ctx, channel ->
                action(ctx, channel)
            }
        }
    }

    /**
     * List command using [context]
     */
    abstract fun list(context: Context)

    /**
     * Add command using [context] and [channel]
     */
    abstract fun add(context: Context, channel: TextChannel)

    /**
     * Remove command using [context] and [channel]
     */
    abstract fun remove(context: Context, channel: TextChannel)


    private fun checkMention(context: Context, action: (context: Context, channel: TextChannel) -> Unit) {
        val mentions = context.mentions
        mentions.firstChannel().ifPresentOrElse({
            action(context, it)
        },
                {
                    context.sendMessage(
                            EmbedUtil.error(
                                    context.translate("phrases.mention.channel.title"),
                                    context.translate("phrases.mention.channel.description")
                            )
                    ).queue()
                })
    }

    protected fun formatChannels(context: Context, channels: Collection<Long>): String {
        return channels.joinToString(prefix = "`", separator = "`, `", postfix = "`") { context.guild.getTextChannelById(it).name }
    }

    protected fun checkEnabled(
            context: Context,
            action: (guild: RegnumGuild) -> Unit,
            predicate: (guild: RegnumGuild) -> Boolean,
            errorTitle: String,
            errorDescription: String
    ) {
        val guild = context.regnumGuild()
        if (predicate(guild)) {
            action(guild)
        } else {
            context.sendMessage(
                    EmbedUtil.error(
                            context.translate(errorTitle),
                            context.translate(errorDescription)
                    )
            ).queue()
        }
    }

}

/**
 * Whitelist command.
 */
class WhitelistCommand : ChannelBlockCommand(
        "whitelist",
        arrayOf("whitelist", "wl"),
        "Blocks commands from all channels except from whitelisted channels",
        "Lists all whitelisted channels",
        "Adds a channel to the whitelist",
        "Removes a channel from the whitelist",
        "whitelist"
) {

    override fun list(context: Context) {
        checkEnabled(context) {
            context.sendMessage(
                    EmbedUtil.info(
                            context.translate("command.whitelist.list.title"),
                            formatChannels(context, it.whitelistedChannels)
                    )
            ).queue()
        }
    }

    override fun add(context: Context, channel: TextChannel) {
        checkBlacklist(context) {
            checkChannel(context, channel) {
                it.whitelistChannel(channel.idLong)
                it.saveAsync().thenRun {
                    context.sendMessage(
                            EmbedUtil.success(
                                    context.translate("command.whitelist.add.success.title"),
                                    context.translate("command.whitelist.add.success.description")
                                            .format(channel.asMention)
                            )
                    ).queue()
                }
            }
        }
    }

    override fun remove(context: Context, channel: TextChannel) {
        checkBlacklist(context) {
            checkWhitelistedChannel(context, channel) {
                it.unWhitelistChannel(channel.idLong)
                it.saveAsync().thenRun {
                    context.sendMessage(
                            EmbedUtil.success(
                                    context.translate("command.whitelist.remove.success.title"),
                                    context.translate("command.whitelist.remove.success.description")
                                            .format(channel.asMention)
                            )
                    ).queue()
                }
            }
        }
    }

    private fun checkEnabled(context: Context, action: (guild: RegnumGuild) -> Unit) {
        checkEnabled(
                context,
                action,
                { it.usesWhitelist() },
                "command.whitelist.disabled.title",
                "command.whitelist.disabled.description"
        )
    }

    private fun checkBlacklist(context: Context, action: (guild: RegnumGuild) -> Unit) {
        checkEnabled(
                context,
                action,
                { !it.usesBlacklist() },
                "command.whitelist.blacklist.title",
                "command.whitelist.blacklist.description"
        )
    }

    private fun checkChannel(context: Context, channel: TextChannel, action: () -> Unit) {
        checkEnabled(
                context,
                { action() },
                { !isWhitelisted(context, channel) },
                "command.whitelist.add.whitelisted.title",
                "command.whitelist.add.whitelisted.description"
        )
    }

    private fun checkWhitelistedChannel(context: Context, channel: TextChannel, action: () -> Unit) {
        checkEnabled(
                context,
                { action() },
                { isWhitelisted(context, channel) },
                "command.whitelist.remove.whitelisted.title",
                "command.whitelist.remove.whitelisted.description"
        )
    }

    private fun isWhitelisted(context: Context, channel: TextChannel): Boolean {
        val guild = context.regnumGuild()
        return channel.idLong in guild.whitelistedChannels
    }
}

/**
 * Blacklist command
 */
class BlacklistCommand : ChannelBlockCommand(
        "Blacklist",
        arrayOf("blacklist", "bl"),
        "Blocks commands from a channel",
        "Lists all blacklisted channels",
        "Adds a channel to the blacklist",
        "Removes a channel from the blacklist",
        "blacklist"
) {

    override fun list(context: Context) {
        checkEnabled(context) {
            context.sendMessage(
                    EmbedUtil.info(
                            context.translate("command.blacklist.list.title"),
                            formatChannels(context, it.blacklistedChannels)
                    )
            ).queue()
        }
    }

    override fun add(context: Context, channel: TextChannel) {
        checkWhitelist(context) {
            checkChannel(context, channel) {
                it.blockChannel(channel.idLong)
                it.saveAsync().thenRun {
                    context.sendMessage(
                            EmbedUtil.success(
                                    context.translate("command.blacklist.add.success.title"),
                                    context.translate("command.blacklist.add.success.description")
                                            .format(channel.asMention)
                            )
                    ).queue()
                }
            }
        }
    }

    override fun remove(context: Context, channel: TextChannel) {
        checkWhitelist(context) {
            checkBlacklistedChannel(context, channel) {
                it.unBlockChannel(channel.idLong)
                it.saveAsync().thenRun {
                    context.sendMessage(
                            EmbedUtil.success(
                                    context.translate("command.blacklist.remove.success.title"),
                                    context.translate("command.blacklist.remove.success.description")
                                            .format(channel.asMention)
                            )
                    ).queue()
                }
            }
        }
    }

    private fun checkEnabled(context: Context, action: (guild: RegnumGuild) -> Unit) {
        checkEnabled(
                context,
                action,
                { it.usesBlacklist() },
                "command.blacklist.disabled.title",
                "command.blacklist.disabled.description"
        )
    }

    private fun checkWhitelist(context: Context, action: (guild: RegnumGuild) -> Unit) {
        checkEnabled(
                context,
                action,
                { !it.usesWhitelist() },
                "command.blacklist.whitelist.title",
                "command.blacklist.whitelist.description"
        )
    }

    private fun checkChannel(context: Context, channel: TextChannel, action: () -> Unit) {
        checkEnabled(
                context,
                { action() },
                { !isBlacklisted(context, channel) },
                "command.blacklist.add.blacklisted.title",
                "command.blacklist.add.blacklisted.description"
        )
    }

    private fun checkBlacklistedChannel(context: Context, channel: TextChannel, action: () -> Unit) {
        checkEnabled(
                context,
                { action() },
                { isBlacklisted(context, channel) },
                "command.blacklist.remove.blacklisted.title",
                "command.blacklist.remove.blacklisted.description"
        )
    }

    private fun isBlacklisted(context: Context, channel: TextChannel): Boolean {
        val guild = context.regnumGuild()
        return channel.idLong in guild.blacklistedChannels
    }

}

