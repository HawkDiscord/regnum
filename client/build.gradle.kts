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

import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.LinkMapping
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.dokka").version("0.9.17")
    id("com.jfrog.bintray").version("1.8.4")
    kotlin("jvm") version "1.3.21"
    java
    `maven-publish`
}

group = "cc.hawkbot.regnum"
version = "1.0-SNAPSHOT"
val archivesBasename = "regnum.client"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {

    // Discord
    @Suppress("SpellCheckingInspection")
    compile("net.dv8tion:JDA:4.ALPHA.0_54")

    // Regnum
    compile(project(":shared"))

    // Server
    implementation("org.java-websocket:Java-WebSocket:1.4.0")

    // Util
    compile("com.google.guava:guava:27.0.1-jre")

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testCompile("junit", "junit", "4.12")
}

val dokkaJar by tasks.creating(Jar::class)

val sourcesJar by tasks.creating(Jar::class)

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}

artifacts {
    add("archives", dokkaJar)
    add("archives", sourcesJar)
}


tasks {
    "dokka"(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "public"
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
        externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
            url = uri("https://pages.hawkbot.cc/shared/javadoc/shared/").toURL()
        })
        externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
            url = uri("https://ci.dv8tion.net/job/JDA4-Alpha/javadoc/").toURL()
            packageListUrl = uri("https://gist.githubusercontent.com/DRSchlaubi/3d1d0aaa5c01963dcd4d0149c841c896/raw/22141759fbab1e38fd2381c3e4f97616ecb43fc8/package-list").toURL()
        })
    }
    val buildDir = File("build")
    "sourcesJar"(Jar::class) {
        classifier = "sources"
        destinationDir = buildDir
        from(sourceSets["main"].allSource)
    }
    "dokkaJar"(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        classifier = "javadoc"
        destinationDir = buildDir
        from(tasks["dokka"])
    }
    "jar"(Jar::class) {
        destinationDir = buildDir
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("mavenJava")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = archivesBasename
        userOrg = "drschlaubi"
        setLicenses("GPL-3.0")
        vcsUrl = "https://github.com/DRSchlaubi/regnum.git"
        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            name = project.version as String
        })
    })
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_HIGHER
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
