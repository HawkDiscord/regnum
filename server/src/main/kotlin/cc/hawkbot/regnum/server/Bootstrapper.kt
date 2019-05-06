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

package cc.hawkbot.regnum.server

import cc.hawkbot.regnum.server.core.internal.ServerImpl
import cc.hawkbot.regnum.util.logging.Logger
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.ConfigurationSource
import org.apache.logging.log4j.core.config.Configurator

/**
 * Regnum servers main function.
 * @param args the program arguments
 */
fun main(args: Array<String>) {

    val launchedAt = System.currentTimeMillis()

    val options = Options()
            .addOption(
                    Option.builder("D")
                            .longOpt("debug")
                            .desc("Enabled debug mode")
                            .build()
            ).addOption(
                    Option.builder("L")
                            .longOpt("log-level")
                            .hasArg()
                            .desc("Logging level")
                            .build()
            ).addOption(
                    Option.builder("ND")
                            .longOpt("no-discord")
                            .desc("Disabled built-in Discord bot")
                            .build()
            )

    val parser = DefaultParser()
    val cmd = parser.parse(options, args)

    Configurator.setRootLevel(Level.toLevel(cmd.getOptionValue("L"), Level.INFO))
    Configurator.initialize(ClassLoader.getSystemClassLoader(), ConfigurationSource(Thread.currentThread().contextClassLoader.getResourceAsStream("log4j2.xml")))

    Logger.getLogger().info("[Launcher] Starting Regnum server!")

    // Start server
    ServerImpl(launchedAt, cmd.hasOption("D"), cmd.hasOption("ND"))
}