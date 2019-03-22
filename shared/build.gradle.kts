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

import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.LinkMapping
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.dokka").version("0.9.17")
    java
    kotlin("jvm").version("1.3.21")
}

group = "cc.hawkbot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {

    // Logging
    implementation("org.slf4j:slf4j-api:1.7.25")
    // Only needed in server
    implementation("org.apache.logging.log4j:log4j-core:2.11.0")

    @Suppress("SpellCheckingInspection")
    compile("net.dv8tion:JDA:4.ALPHA.0_54")

    // Util
    @Suppress("SpellCheckingInspection")
    compile("com.electronwill.night-config:yaml:3.5.0")
    compile("com.fasterxml.jackson.core:jackson-databind:2.9.7")
    compile("io.sentry:sentry:1.7.16")

    // Database
    compile("com.datastax.cassandra:cassandra-driver-core:3.6.0")
    compile("com.datastax.cassandra:cassandra-driver-mapping:3.6.0")
    compile("com.datastax.cassandra:cassandra-driver-extras:3.6.0")


    // Kotlin
    implementation(kotlin("stdlib-jdk8"))

    // Tests
    testCompile("junit", "junit", "4.12")
}

tasks {
    "dokka"(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
        jdkVersion = 8
        reportUndocumented = true
        impliedPlatforms = mutableListOf("JVM")
        sourceDirs = files("src/main/kotlin", "src/main/java")
        sourceDirs.forEach {
            val relativePath = rootDir.toPath().relativize(it.toPath()).toString()
            linkMapping(delegateClosureOf<LinkMapping> {
                dir = it.absolutePath
                url = "https://github.com/DRSchlaubi/regnum/tree/master/$relativePath"
                suffix = "#L"
            })
        }
        externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
            url = uri("https://www.slf4j.org/api/").toURL()
        })
        externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
            url = uri("http://fasterxml.github.io/jackson-databind/javadoc/2.9/").toURL()
        })
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_HIGHER
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}