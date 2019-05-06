///*
// * Regnum - A Discord bot clustering system made for Hawk
// *
// * Copyright (C) 2019  Michael Rittmeister
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see https://www.gnu.org/licenses/.
// */
//
//import cc.hawkbot.regnum.client.Regnum;
//import cc.hawkbot.regnum.client.command.Group;
//import cc.hawkbot.regnum.client.command.context.Arguments;
//import cc.hawkbot.regnum.client.command.context.Context;
//import cc.hawkbot.regnum.client.command.permission.CommandPermissions;
//import cc.hawkbot.regnum.client.command.translation.defaults.PropertyLanguage;
//import cc.hawkbot.regnum.client.config.CassandraConfig;
//import cc.hawkbot.regnum.client.config.CommandConfig;
//import cc.hawkbot.regnum.client.config.GameAnimatorConfig;
//import cc.hawkbot.regnum.client.core.discord.GameAnimator;
//import cc.hawkbot.regnum.client.events.websocket.WebSocketMessageEvent;
//import cc.hawkbot.regnum.client.standalone.StandaloneRegnumBuilder;
//import cc.hawkbot.regnum.client.standalone.config.StandaloneConfig;
//import cc.hawkbot.regnum.entities.packets.HeartBeatAckPacket;
//import cc.hawkbot.regnum.util.logging.Logger;
//import net.dv8tion.jda.api.hooks.SubscribeEvent;
//import org.jetbrains.annotations.NotNull;
//import org.slf4j.event.Level;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Locale;
//
///**
// * Util which launches client just for testing.
// */
//@SuppressWarnings("all")
//public class StandaloneLauncher {
//
//    private final Regnum regnum;
//
//    private StandaloneLauncher(String token) {
//        Logger.LOG_LEVEL = Level.DEBUG;
//        StandaloneRegnumBuilder builder = new StandaloneRegnumBuilder();
//        builder.setCommandConfig(
//                new CommandConfig("hw!", new PropertyLanguage(new Locale("en", "US"), "locales/en_US.properties", StandardCharsets.UTF_8))
//                        .registerCommands(new Command())
//        )
//                .setCassandraConfig(
//                        new CassandraConfig("test", "cassandra", "cassandra", Collections.singletonList("127.0.0.1"))
//                )
//                .setGameAnimatorConfig(new GameAnimatorConfig(Arrays.asList(GameAnimator.Game.Companion.compile("ONLINE:0:playing"))))
//                .registerEvents(this);
//        builder.setStandaloneConfig(StandaloneConfig.Companion.auto(token));
//        regnum = builder.build();
//    }
//
//    public static void main(String[] args) {
//        new StandaloneLauncher(args[0]);
//    }
//
//    @SubscribeEvent
//    @SuppressWarnings("unused")
//    private void onMessage(WebSocketMessageEvent event) {
//        if (event.payload().getType().equals(HeartBeatAckPacket.IDENTIFIER)) {
//            System.out.println("PING: " + regnum.getWebsocket().ping());
//        }
//    }
//
//    private static class Command extends cc.hawkbot.regnum.client.command.Command {
//        public Command() {
//            super(Group.getEMPTY(), "test", new String[]{"test"}, new CommandPermissions(false, false, true, "test"), "", "", "");
//        }
//
//        @Override
//        public void execute(@NotNull Arguments args, @NotNull Context context) {
//            context.sendMessage(
//                    "Mentions" + context.getMentions().list() + "\n" +
//                            "Args: " + String.join(" ", context.getArgs().getArray())
//
//            ).queue();
//            if (!args.isEmpty() && args.get(0).equals("error")) {
//                Integer.parseInt("OMA");
//            }
//        }
//    }
//}
