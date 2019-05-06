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
import org.jetbrains.dokka.gradle.LinkMapping
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.dokka").version("0.9.18")
    id("com.jfrog.bintray").version("1.8.4")
    kotlin("jvm").version("1.3.21")
    java
    `maven-publish`
}

group = "cc.hawkbot.regnum"
val archivesBasename = "shared"
version = rootProject.version
repositories {
    mavenCentral()
    jcenter()
}

dependencies {

    // Logging
    implementation("org.slf4j", "slf4j-api", project.ext["slf4jVersion"] as String)
    // Only needed in server
    implementation("org.apache.logging.log4j", "log4j-core", project.ext["log4jVersion"] as String)
    
    // Util
    @Suppress("SpellCheckingInspection")
    compile("com.electronwill.night-config", "yaml", project.ext["nightconfigVersion"] as String)
    compile("com.fasterxml.jackson.core", "jackson-databind", project.ext["jacksonVersion"] as String)
    compile("io.sentry:sentry:1.7.16")

    // Database
    compile("com.datastax.cassandra", "cassandra-driver-core", project.ext["cassandraVersion"] as String)
    compile("com.datastax.cassandra", "cassandra-driver-mapping", project.ext["cassandraVersion"] as String)
    compile("com.datastax.cassandra", "cassandra-driver-extras", project.ext["cassandraVersion"] as String)


    // Kotlin
    implementation(kotlin("stdlib-jdk8"))

    // Tests
    testCompile("junit", "junit", "4.12")
}

val dokkaJar by tasks.creating(Jar::class)

val sourcesJar by tasks.creating(Jar::class)

artifacts {
    add("archives", dokkaJar)
    add("archives", sourcesJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(sourcesJar)
            artifact(dokkaJar)
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("mavenJava")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = "regnum-shared"
        userOrg = "hawk"
        setLicenses("GPL-3.0")
        vcsUrl = "https://github.com/DRSchlaubi/regnum.git"
        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            name = project.version as String
        })
    })
}

tasks {
    task("buildArtifacts")
    dokka {
        outputFormat = "html"
        outputDirectory = (project.ext["docsDir"] as File).absolutePath
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
    val buildDir = project.ext["buildDir"] as File
    "sourcesJar"(Jar::class) {
        archiveClassifier.set("sources")
        destinationDirectory.set(buildDir)
        from(sourceSets["main"].allSource)
    }
    "dokkaJar"(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        archiveClassifier.set("javadoc")
        destinationDirectory.set(buildDir)
        from(dokka)
    }
    "jar"(Jar::class) {
        destinationDirectory.set(buildDir)
    }
    "buildArtifacts"(Task::class) {
        dependsOn(sourcesJar, dokkaJar, jar)
    }

}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_12
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}