import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.jfrog.bintray").version("1.8.4")
    kotlin("jvm") version "1.3.21"
    java
    `maven-publish`
}

group = "cc.hawkbot.client"
version = "0.0.1"
val archivesBasename = "regnum.standalone"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile(project(":client"))
    implementation(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_HIGHER
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}

bintray {
    user = "drschlaubi"
    key = "1b56e7110a4d2d2983d988fa5f1736fff4f6a134"
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