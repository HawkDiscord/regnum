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

package cc.hawkbot.regnum.entities.packets;

@SuppressWarnings("UnusedReturnValue")
public class MetricsPacket implements Packet {

    public static final String IDENTIFIER = "METRICS";

    private long discordRestPing;
    private long discordGatewayPing;
    private long usedMemory;
    private long availableMemory;
    private long cpuUsage;
    private int cpus;
    private long guilds;
    private long users;

    public MetricsPacket() {

    }

    public MetricsPacket(long discordRestPing, long discordGatewayPing, long usedMemory, long availableMemory, long cpuUsage, int cpus, long guilds, long users) {
        this.discordRestPing = discordRestPing;
        this.discordGatewayPing = discordGatewayPing;
        this.usedMemory = usedMemory;
        this.availableMemory = availableMemory;
        this.cpuUsage = cpuUsage;
        this.cpus = cpus;
        this.guilds = guilds;
        this.users = users;
    }

    public long getDiscordRestPing() {
        return discordRestPing;
    }

    public long getDiscordGatewayPing() {
        return discordGatewayPing;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public long getAvailableMemory() {
        return availableMemory;
    }

    public long getCpuUsage() {
        return cpuUsage;
    }

    public int getCpus() {
        return cpus;
    }

    public long getGuilds() {
        return guilds;
    }

    public long getUsers() {
        return users;
    }
}
