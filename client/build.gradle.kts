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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.21"
}

group = "cc.hawkbot"
version = "1.0-SNAPSHOT"
group = "cc.hawkbot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://jitpack.io") }
}

dependencies {

    // Discord
    @Suppress("SpellCheckingInspection")
    implementation("net.dv8tion:JDA:4.ALPHA.0_50")

    // Regnum
    implementation(project(":shared"))

    // Server
    implementation("org.java-websocket:Java-WebSocket:1.4.0")

    // Util
    implementation("com.google.guava:guava:27.0.1-jre")

    implementation(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}