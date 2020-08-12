import com.palantir.gradle.docker.DockerExtension
import net.minecraftforge.gradle.user.UserBaseExtension
import net.nemerosa.versioning.VersioningExtension
import net.nemerosa.versioning.tasks.VersionFileTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Libraries required by MCP plugin
 */
buildscript {
    repositories {
        maven("https://files.minecraftforge.net/maven")
        maven("https://repo.spongepowered.org/maven")
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:2.3-SNAPSHOT")
    }
}

plugins {
    // plugin to create file with current git status
    id(Plugins.VERSIONING) version "2.8.2" apply false

    // plugin used to create docker images
    id(Plugins.DOCKER) version "0.22.1" apply false

    // plugin used to create fat jars
    id(Plugins.SHADOW) version "5.2.0" apply false

    // plugin used for developing Sponge plugins
    id(Plugins.SPONGE) version "0.9.0" apply false

    // plugin used to decompile NMS
    id(Plugins.NMS) version "2.2-6" apply false

    kotlin("jvm") version "1.3.72" apply false
    idea
    `java-library`
}

/*
    To avoid writing the same code multiple times, shared configurations and configured below.
    This assumes that all projects at least use java
 */

subprojects {
    apply(plugin = Plugins.VERSIONING)
    apply(plugin = Plugins.SHADOW)
    apply(plugin = "org.gradle.java-library")

    repositories {
        mavenCentral()
        jcenter()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.dmulloy2.net/nexus/repository/public/")
        maven("https://jitpack.io")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }

    configure<VersioningExtension> {
        dirtySuffix = "-custom"
        noWarningOnDirty = true
    }

    dependencies {
        compileOnly(Deps.LOMBOK)
        annotationProcessor(Deps.LOMBOK)

        testImplementation(Deps.MOCKITO)
        testImplementation(Deps.JUPITER_API)
        testImplementation(Deps.JUPITER_PARAMS)
        testRuntimeOnly(Deps.JUPITER_ENGINE)
    }

    tasks {
        withType<JavaCompile>().configureEach {
            options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:deprecation"))
        }

        named<VersionFileTask>("versionFile") {
            file = File(buildDir, "resources/main/version.properties")
        }

        named<Test>("test") {
            useJUnitPlatform()
        }

        named("assemble") {
            dependsOn("shadowJar")
        }

        named("shadowJar") {
            dependsOn("jar")
        }

        named("jar") {
            dependsOn("versionFile")
        }
    }
}

/**
 * Kotlin projects
 */
projects("bungeecord", "utils", "sponge", "lobby") {
    apply(plugin = "kotlin")

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        testImplementation(Deps.MOCKITO_KOTLIN)
    }

    tasks {
        withType<KotlinCompile>().configureEach {
            kotlinOptions.suppressWarnings = true
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}


/**
 * Sponge projects
 */
projects("sponge", "core", "lobby") {
    apply(plugin = Plugins.SPONGE)

    dependencies {
        implementation(Deps.SPONGE_API)
    }
}

/**
 * NMS Projects
 */
projects("sponge") {
    apply(plugin = Plugins.NMS)

    configure<UserBaseExtension> {
        version = "1.12.2"
        mappings = "snapshot_20180131"
        makeObfSourceJar = false
    }
}

/**
 * Docker configuration
 */
projects("bungeecord", "service", "core", "lobby") {
    apply(plugin = Plugins.DOCKER)

    configure<DockerExtension> {
        // this specifies files to be copied to build folder
        copySpec.into(".") {
            from(".") { exclude("build/", "src/") }
            from(".") { include("build/libs/*") }
        }
    }

    tasks {
        named("docker") {
            dependsOn("build")
        }

        named("dockerPrepare") {
            dependsOn("shadowJar")
        }
    }
}