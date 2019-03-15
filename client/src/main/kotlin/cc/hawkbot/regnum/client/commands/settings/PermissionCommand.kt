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
import cc.hawkbot.regnum.client.entities.permission.PermissionNode
import cc.hawkbot.regnum.client.util.filterAndExtract
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
        TODO("not implemented")
    }

    private class AddCommand : SubCommand("Add", arrayOf("add", "assign"), usage = "<@User/@Role <node>", exampleUsage = "@Schlaubi#1337 command.help", description = "Adds the permission to use a command") {
        override fun execute(args: Arguments, context: Context) {
            checkMention(context) {
                checkArgs(context) { node ->
                    val manager = context.regnum.permissionManager
                    if (manager.nodeExists(it, node)) {
                        return@checkArgs context.sendMessage("Node exists").queue()
                    }
                    manager.createPermissionNode(it, node).thenAccept { holder ->
                        context.sendMessage("Added ${holder.permissionNode} to ${holder.id}").queue()
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
                        permission.negate()
                        manager.updateNode(permission).thenRun {
                            context.sendMessage("Negated $node to ${it.id}").queue()
                        }
                    } else {
                        manager.createPermissionNode(it, node).thenAccept { holder ->
                            context.sendMessage("Added ${holder.permissionNode} to ${holder.id}").queue()
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
                        return@checkArgs context.sendMessage("!Node exists").queue()
                    }
                    manager.getNode(it, node).deleteAsync().thenRun {
                        context.sendMessage("Removed $node from ${it.id}").queue()
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
                val allowedPermissions = mutableListOf<PermissionNode>()
                val negatedPermissions = permissions.filterAndExtract(allowedPermissions) { it.isNegated }
                context.sendMessage("Allowed ${stringifyPermissionList(allowedPermissions)}, denied ${stringifyPermissionList(negatedPermissions)}").queue()
            }
        }

        private fun stringifyPermissionList(permissions: Collection<PermissionNode>): String {
            return permissions.joinToString(prefix = "`", postfix = "`", separator = ", ")
        }

    }

}

fun checkMention(context: Context, action: (mention: IPermissionHolder) -> Unit) {
    val mentions = context.mentions
    val holder: IPermissionHolder = when {
        mentions.firstMember().isPresent -> mentions.firstMember().get()
        mentions.firstRole().isPresent -> mentions.firstRole().get()
        else -> {
            context.sendMessage("Please define a holder").queue()
            return
        }
    }
    action(holder)
}

fun checkArgs(context: Context, action: (node: String) -> Unit) {
    val args = context.args
    if (args.size() > 2) {
        return context.sendMessage("PLS define a node").queue()
    }
    action(args[1])
}

