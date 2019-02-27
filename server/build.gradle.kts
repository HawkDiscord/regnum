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

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version("1.3.21")
    id("com.github.johnrengelman.shadow").version("4.0.3")
    java
    application
}

group = "cc.hawkbot"
version = "1.0-SNAPSHOT"

val log4jVersion = "2.11.2"
val cliVersion = "1.4"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    jcenter()
}

dependencies {

    // Regnum
    implementation(project(":shared"))

    // Server
    implementation("io.javalin:javalin:2.6.0")
    @Suppress("SpellCheckingInspection")
    implementation("net.dv8tion:JDA:4.ALPHA.0_50")

    // Logging
    implementation(log4j("core"))
    implementation(log4j("slf4j-impl"))

    // Util
    implementation("commons-cli:commons-cli:$cliVersion")

    implementation(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
}

application {
    mainClassName = "cc.hawkbot.regnum.server.BootstrapperKt"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    "shadowJar"(ShadowJar::class) {
        baseName = project.name
        version = version
        archiveName = "$baseName.$extension"
    }
}

fun log4j(name: String, version: String = log4jVersion): String {
    return "org.apache.logging.log4j:log4j-$name:$version"
}
