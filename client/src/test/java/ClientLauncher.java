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

import cc.hawkbot.regnum.client.Regnum;
import cc.hawkbot.regnum.client.RegnumBuilder;
import cc.hawkbot.regnum.client.config.GameAnimatorConfig;
import cc.hawkbot.regnum.client.config.ServerConfig;
import cc.hawkbot.regnum.client.core.discord.GameAnimator;
import cc.hawkbot.regnum.client.event.EventSubscriber;
import cc.hawkbot.regnum.util.logging.Logger;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.event.Level;

import java.util.List;

/**
 * Util which launches client just for testing.
 */
@SuppressWarnings("WeakerAccess")
public class ClientLauncher {

    private final Regnum regnum;

    private ClientLauncher() {
        Logger.LOG_LEVEL = Level.DEBUG;
        var builder = new RegnumBuilder()
                .setServerConfig(
                        new ServerConfig("ws://localhost:7001/ws", "SUPER-SECRET-TOKEN")
                ).
                        setGameAnimatorConfig(new GameAnimatorConfig(List.of(new GameAnimator.Game(0, "ONLINE", "TEST LEL"))))
                .registerEvents(this);
        regnum = builder.build();
    }

    public static void main(String[] args) {
        new ClientLauncher();
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    private void onMessage(GuildMessageReceivedEvent event) {
        System.out.println(event.getMessage().getContentDisplay());
    }
}
