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

@file:Suppress("SpellCheckingInspection")

package cc.hawkbot.regnum.client.commands.settings

import cc.hawkbot.regnum.client.command.Command
import cc.hawkbot.regnum.client.command.Group
import cc.hawkbot.regnum.client.command.SubCommand
import cc.hawkbot.regnum.client.command.context.Arguments
import cc.hawkbot.regnum.client.command.context.Context
import cc.hawkbot.regnum.client.command.permission.CommandPermissions
import cc.hawkbot.regnum.client.entities.permission.PermissionNode
import cc.hawkbot.regnum.client.util.EmbedUtil
import cc.hawkbot.regnum.client.util.filterAndExtract
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.entities.IPermissionHolder
import net.dv8tion.jda.api.entities.Member

class PermissionCommand : Command(Group.SETTINGS, "Permissions", arrayOf("permissions", "perms"), CommandPermissions(node = "permissions", serverAdminExclusive = true), "<list/remove/deny> <@User/@Role> [node]", "", "") {
    init {
        registerSubCommands(
                ListCommand(),
                AddCommand(),
                DenyCommand(),
                RemoveCommand()
        )
    }

    override fun execute(args: Arguments, context: Context) {
        return context.sendHelp().queue()
    }

    private class AddCommand : SubCommand("Add", arrayOf("add", "assign"), usage = "<@User/@Role <node>", exampleUsage = "@Schlaubi#1337 command.help", description = "Adds the permission to use a command") {
        override fun execute(args: Arguments, context: Context) {
            checkMention(context) {
                checkArgs(context) { node ->
                    val manager = context.regnum.permissionManager
                    if (manager.nodeExists(it, node)) {
                        val permission = manager.getNode(it, node)
                        if (permission.isNegated) {
                            permission.allow()
                            permission.saveAsync().thenRun {
                                context.sendMessage(
                                        EmbedUtil.success(
                                                context.translate("command.permission.add.allowed.title"),
                                                context.translate("command.permission.add.allowed.description")
                                                        .format(node, (it as IMentionable).asMention)
                                        )
                                ).queue()
                            }
                            return@checkArgs
                        }
                        return@checkArgs context.sendMessage(
                                EmbedUtil.error(
                                        context.translate("command.permission.add.exists.title"),
                                        context.translate("command.permission.add.exists.description")
                                                .format(node, (it as IMentionable).asMention)
                                )
                        ).queue()
                    }
                    manager.createPermissionNode(it, node).thenAccept { permission ->
                        context.sendMessage(
                                EmbedUtil.success(
                                        context.translate("command.permission.add.success.title"),
                                        context.translate("command.permission.add.success.description")
                                                .format(permission.permissionNode, (it as IMentionable).asMention)
                                )
                        ).queue()
                    }
                }
            }
        }
    }

    private class DenyCommand : SubCommand("Deny", arrayOf("deny", "revoke"), usage = "<@User/@Role <node>", exampleUsage = "@Schlaubi#1337 command.help", description = "Denies the permission to use a command") {
        override fun execute(args: Arguments, context: Context) {
            checkMention(context) {
                checkArgs(context) { node ->
                    val manager = context.regnum.permissionManager
                    if (manager.nodeExists(it, node)) {
                        val permission = manager.getNode(it, node)
                        if (permission.isNegated) {
                            return@checkArgs context.sendMessage(
                                    EmbedUtil.error(
                                            context.translate("command.permission.deny.exists.title"),
                                            context.translate("command.permission.deny.exists.description")
                                    )
                            ).queue()
                        }
                        permission.negate()
                        permission.saveAsync().thenRun {
                            context.sendMessage(
                                    EmbedUtil.success(
                                            context.translate("command.permission.deny.negated.title"),
                                            context.translate("command.permission.deny.negated.description")
                                                    .format(node, (it as IMentionable).asMention)
                                    )
                            ).queue()
                        }
                        return@checkArgs
                    } else {
                        manager.createPermissionNode(it, node, true).thenAccept { permission ->
                            context.sendMessage(
                                    EmbedUtil.success(
                                            context.translate("command.permission.deny.success.title"),
                                            context.translate("command.permission.deny.success.description")
                                                    .format(permission.permissionNode, (it as IMentionable).asMention)
                                    )
                            ).queue()
                        }
                    }
                }
            }
        }
    }

    private class RemoveCommand : SubCommand("Remove", arrayOf("remove", "delete"), usage = "<@User/@Role <node>", exampleUsage = "@Schlaubi#1337 command.help", description = "Removes the permission to use a command") {
        override fun execute(args: Arguments, context: Context) {
            checkMention(context) {
                checkArgs(context) { node ->
                    val manager = context.regnum.permissionManager
                    if (!manager.nodeExists(it, node)) {
                        return@checkArgs context.sendMessage(
                                EmbedUtil.error(
                                        context.translate("command.permission.remove.unkown.title"),
                                        context.translate("command.permission.remove.unkown.description")
                                )
                        ).queue()
                    }
                    manager.getNode(it, node).deleteAsync()
                            .thenRun {
                                context.sendMessage(
                                        EmbedUtil.success(
                                                context.translate("command.permission.remove.title"),
                                                context.translate("command.permission.remove.description")
                                                        .format(node, (it as IMentionable).asMention)
                                        )
                                ).queue()
                            }
                }
            }
        }
    }

    private class ListCommand : SubCommand("List", arrayOf("list", "ls"), usage = "<@User/@Role>", exampleUsage = "@Schlaubi#1337", description = "Lists all permission of a permission holder") {
        override fun execute(args: Arguments, context: Context) {
            checkMention(context) { holder ->
                val manager = context.regnum.permissionManager
                val permissions = manager.getNodes(holder).toMutableList()
                if (holder is Member) {
                    holder.roles.forEach {
                        permissions.addAll(manager.getNodes(it))
                    }
                }
                if (permissions.isEmpty()) {
                    return@checkMention context.sendMessage(
                            EmbedUtil.error(
                                    context.translate("command.permission.list.empty.title"),
                                    context.translate("command.permission.list.empty.description")
                            )
                    ).queue()
                }
                val allowedPermissions = mutableListOf<PermissionNode>()
                val negatedPermissions = permissions.filterAndExtract(allowedPermissions) { it.isNegated }
                val embed = EmbedUtil.info(
                        context.translate("command.permission.list.title"),
                        context.translate("command.permission.list.description")
                                .format((holder as IMentionable).asMention)
                )
                if (allowedPermissions.isNotEmpty()) {
                    embed.addField(
                            context.translate("command.permission.list.allowed"),
                            stringifyPermissionList(allowedPermissions),
                            false
                    )
                }
                if (negatedPermissions.isNotEmpty())
                    embed.addField(
                            context.translate("command.permission.list.negated"),
                            stringifyPermissionList(negatedPermissions),
                            false
                    )
                context.sendMessage(embed).queue()
            }
        }

        private fun stringifyPermissionList(permissions: Collection<PermissionNode>): String {
            return permissions.joinToString(prefix = "`", postfix = "`", separator = "`, `") { it.permissionNode }
        }

    }

}

fun checkMention(context: Context, action: (mention: IPermissionHolder) -> Unit) {
    val mentions = context.mentions
    val holder: IPermissionHolder = when {
        mentions.firstMember().isPresent -> mentions.firstMember().get()
        mentions.firstRole().isPresent -> mentions.firstRole().get()
        else -> {
            return context.sendMessage(
                    EmbedUtil.error(
                            context.translate("command.permission.notargt.title"),
                            context.translate("command.permission.notargt.description")
                    )
            ).queue()
        }
    }
    action(holder)
}

fun checkArgs(context: Context, action: (node: String) -> Unit) {
    val args = context.args
    if (args.size() > 2) {
        return context.sendMessage(
                EmbedUtil.error(
                        context.translate("command.permissions.error.nodemissing.title"),
                        context.translate("command.permissions.error.nodemissing.description")
                )
        ).queue()
    }
    action(args[1])
}

