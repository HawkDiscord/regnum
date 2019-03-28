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
}

dependencies {

    compile(project(":shared"))

    compile("de.foryasee:plugins:1.1.0")

    // Server
    implementation("io.javalin:javalin:2.6.0")
    @Suppress("SpellCheckingInspection")
    implementation("net.dv8tion:JDA:4.ALPHA.0_50")

    implementation(kotlin("stdlib-jdk8"))
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
        externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
            url = uri("https://pages.hawkbot.cc/shared/javadoc/shared/").toURL()
        })
        externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
            url = uri("https://ci.dv8tion.net/job/JDA4-Alpha/javadoc/").toURL()
            packageListUrl = uri("https://gist.githubusercontent.com/DRSchlaubi/3d1d0aaa5c01963dcd4d0149c841c896/raw/22141759fbab1e38fd2381c3e4f97616ecb43fc8/package-list").toURL()
        })
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_HIGHER
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}