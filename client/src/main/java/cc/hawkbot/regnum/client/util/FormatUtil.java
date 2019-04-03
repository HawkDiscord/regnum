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

package cc.hawkbot.regnum.client.util;

import cc.hawkbot.regnum.client.Regnum;
import cc.hawkbot.regnum.client.command.CommandParser;
import cc.hawkbot.regnum.client.command.Group;
import cc.hawkbot.regnum.client.command.ICommand;
import cc.hawkbot.regnum.client.command.SubCommand;
import cc.hawkbot.regnum.client.command.permission.IPermissions;
import cc.hawkbot.regnum.client.entities.RegnumGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * Some useful formatters.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class FormatUtil {

    /**
     * Converts an {@link MessageEmbed} into plain text.
     *
     * @param embed The embed
     * @return The embed as plain text
     */
    @NotNull
    public static String stringifyEmbed(@NotNull MessageEmbed embed) {
        var builder = new StringBuilder();
        if (embed.getTitle() != null) {
            builder.append("**").append(embed.getTitle()).append("**").append("\n");
        }
        if (embed.getDescription() != null) {
            builder.append(embed.getDescription()).append("\n");
        }
        embed.getFields().forEach(it -> builder.append("**").append(it.getName()).append("**").append("\n")
                .append(it.getValue()).append("\n"));
        if (embed.getFooter() != null) {
            builder.append("_").append(embed.getFooter()).append("_");
        }
        var string = builder.toString();
        if (string.length() > 1024) {
            return "This message is to long to be sent as plain text please give me MESSAGE_EMBED_LINKS"
                    + " permission";
        }
        return string;
    }

    /**
     * Formats a list of commands in a group.
     *
     * @param group the group that needs to be formatted
     * @return the formatted group
     * @see FormatUtil#generateCommandList(Collection)
     */
    @NotNull
    public static String generateCommandList(@NotNull Group group, @NotNull CommandParser parser) {
        return generateCommandList(group.commands(parser));
    }

    /**
     * Formats a list of commands for help message.
     *
     * @param commands a collection of commands
     * @return the formatted collection
     */
    @NotNull
    public static String generateCommandList(@NotNull Collection<ICommand> commands) {
        if (commands.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        commands.stream().distinct().forEach(command ->
                builder.append("`")
                        .append(command.name())
                        .append("`")
                        .append(", "));
        // Replace last ,
        builder.replace(builder.lastIndexOf(", "), builder.lastIndexOf(", ") + 1, "");
        return builder.toString();
    }

    /**
     * Formats the help message of a command.
     *
     * @param command the command
     * @return the formatted embed
     */
    @NotNull
    public static EmbedBuilder formatCommand(@NotNull ICommand command, @NotNull RegnumGuild guild,
                                             @NotNull Regnum regnum) {
        var embedBuilder = new EmbedBuilder()
                .setTitle(command.getDisplayName() + " - Help")
                .setDescription(command.getDescription());
        embedBuilder = embedBuilder.addField("Usage", formatUsage(command.getUsage(),
                command, guild, regnum), false);
        embedBuilder = embedBuilder.addField("Example usage", formatUsage(command.getExampleUsage(),
                command, guild, regnum), false);
        embedBuilder = embedBuilder.addField("Aliases", "`" + Arrays.toString(command.getAliases()).replace("[", "").replace("]", "") + "`", false);
        embedBuilder = embedBuilder.addField("Required permissions", parsePermissions(command.getPermissions()), false);
        if (!(command instanceof SubCommand) && !command.getSubCommandAssociations().isEmpty()) {
            var buf = new StringBuilder();
            command.getSubCommandAssociations().values().stream().distinct().forEach(it -> buf.append(formatUsage(it.getUsage(), it, guild, regnum)).append(System.lineSeparator()));
            embedBuilder = embedBuilder.addField("Subcommands", buf.toString(), false);
        }
        return embedBuilder;
    }

    private static String parsePermissions(IPermissions permissions) {
        if (permissions.getPublic()) {
            return "public";
        } else if (permissions.getBotOwnerExclusive()) {
            return "bot-owner";
        } else if (permissions.getServerAdminExclusive()) {
            return "server-admin or `" + permissions.getNode() + "`";
        } else
            return permissions.getNode();
    }

    /**
     * Formats the usage of a command in prefixcommand usage.
     *
     * @param usage   the usage
     * @param command the command
     * @param guild   the guild the command gets executed on
     * @param regnum  the Regnum instance
     * @return the formatted usage
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static String formatUsage(@NotNull String usage, @NotNull ICommand command, @NotNull RegnumGuild guild,
                                     @NotNull Regnum regnum) {
        var buf = new StringBuilder();
        buf.append(guild.getPrefix());
        if (command instanceof SubCommand) {
            buf.append(((SubCommand) command).getParent().name());
            buf.append(' ');
        }
        buf.append(command.name());
        buf.append(' ');
        buf.append(usage);
        if (command instanceof SubCommand) {
            buf.append(" - ");
            buf.append(command.getDescription());
        }
        return buf.toString();
    }

    /**
     * Formats a list of channels.
     *
     * @param channels the list of channels
     * @return the formatted list
     */
    @NotNull
    public static String formatChannelList(
            @NotNull Collection<GuildChannel> channels) {
        var builder = new StringBuilder();
        channels.forEach(channel -> builder.append("`").append(channel.getName()).append("`")
                .append(", "));
        builder.replace(builder.lastIndexOf(", "), builder.lastIndexOf(", ") + 1, "");
        return builder.toString();
    }


}
