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
import cc.hawkbot.regnum.client.RegnumBuilder;
import cc.hawkbot.regnum.client.command.Group;
import cc.hawkbot.regnum.client.command.context.Arguments;
import cc.hawkbot.regnum.client.command.context.Context;
import cc.hawkbot.regnum.client.command.permission.CommandPermissions;
import cc.hawkbot.regnum.client.command.translation.defaults.PropertyLanguage;
import cc.hawkbot.regnum.client.config.CassandraConfig;
import cc.hawkbot.regnum.client.config.CommandConfig;
import cc.hawkbot.regnum.client.config.ServerConfig;
import cc.hawkbot.regnum.client.events.websocket.WebSocketMessageEvent;
import cc.hawkbot.regnum.entites.packets.HeartBeatAckPacket;
import cc.hawkbot.regnum.util.logging.Logger;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.event.Level;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

@SuppressWarnings("WeakerAccess")
public class ClientLauncher {

    private final Regnum regnum;

    public static void main(String[] args) {
        new ClientLauncher();
    }

    private ClientLauncher() {
        Logger.LOG_LEVEL = Level.DEBUG;
        var builder = new RegnumBuilder()
                .setServerConfig(
                        new ServerConfig("ws://localhost:7001/ws", "SUPER-SECRET-TOKEN")
                );
        builder.setCommandConfig(
                new CommandConfig("hw!", new PropertyLanguage(new Locale("en", "US"), "locales/en_US.properties", StandardCharsets.UTF_8))
                        .registerCommands(new Command())
        )
                .setCassandraConfig(
                        new CassandraConfig("test", "cassandra", "cassandra", Arrays.asList("127.0.0.1"))
                )
                .registerEvents(this);
        regnum = builder.build();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    private void onMessage(WebSocketMessageEvent event) {
        if (event.payload().getType().equals(HeartBeatAckPacket.IDENTIFIER)) {
            System.out.println("PING: " + regnum.getWebsocket().ping());
        }
    }

    private static class Command extends cc.hawkbot.regnum.client.command.Command {
        public Command() {
            super(Group.Companion.empty(), "test", new String[]{"test"}, new CommandPermissions(false, false, true, "test"), "", "", "");
        }

        @Override
        public void execute(@NotNull Arguments args, @NotNull Context context) {
            context.sendMessage(
                    "Mentions" + context.getMentions().list() + "\n" +
                            "Args: " + String.join(" ", context.getArgs().array())

            ).queue();
            if (!args.isEmpty() && args.get(0).equals("error")) {
                Integer.parseInt("OMA");
            }
        }
    }
}
