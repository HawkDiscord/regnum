import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.21"
}

group = "cc.hawkbot.server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://jitpack.io")}
}

dependencies {

    implementation(project(":shared"))

    implementation("de.foryasee:plugins:1.0.1")

    // Server
    implementation("io.javalin:javalin:2.6.0")
    @Suppress("SpellCheckingInspection")
    implementation("net.dv8tion:JDA:4.ALPHA.0_50")

    implementation(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}