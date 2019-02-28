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

import cc.hawkbot.regnum.client.RegnumBuilder;
import cc.hawkbot.regnum.client.command.Group;
import cc.hawkbot.regnum.client.command.context.Arguments;
import cc.hawkbot.regnum.client.command.context.Context;
import cc.hawkbot.regnum.client.command.permission.CommandPermissions;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("WeakerAccess")
public class ClientLauncher {

    public static void main(String[] args) {
        var builder = new RegnumBuilder()
                .setHost("ws://localhost:7000/ws")
                .setToken("SUPER-SECRET-TOKEN");
        builder.registerCommands(new Command());
        builder.setDefaultPrefix("hw!");
        var regnum = builder.build();
    }

    private static class Command extends cc.hawkbot.regnum.client.command.Command {
        public Command() {
            super(Group.Companion.empty(), "test", new String[] {"test"}, new CommandPermissions(false, false, false, "test"), "", "", "");
        }

        @Override
        public void execute(@NotNull Arguments args, @NotNull Context context) {
            context.sendMessage(
                    "Mentions" + context.getMentions().list() + "\n" +
                            "Args: " + String.join(" ", context.getArgs().array())

            ).queue();
        }
    }
}
