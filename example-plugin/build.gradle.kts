import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
}

group = "cc.hawkbot.regnum"
version = "0.0.3"
repositories {
    jcenter()
    mavenCentral()
}

dependencies {

    implementation(project(":plugin"))

    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}