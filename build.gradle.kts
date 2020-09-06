import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.palantir.gradle.docker.DockerExtension
import net.nemerosa.versioning.VersioningExtension
import net.nemerosa.versioning.tasks.VersionFileTask
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // plugin to create file with current git status
    id(Plugins.VERSIONING) version "2.8.2" apply false

    // plugin used to create docker images
    id(Plugins.DOCKER) version "0.22.1" apply false

    // plugin used to create fat jars
    id(Plugins.SHADOW) version "6.0.0" apply false

    // plugin used for developing Sponge plugins
    id(Plugins.SPONGE) version "0.9.0" apply false

    kotlin("jvm") version "1.4.0" apply false
    kotlin("plugin.serialization") version "1.4.0" apply false
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.0" apply false

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
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.dmulloy2.net/nexus/repository/public/")
        maven("https://jitpack.io")
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
subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "kotlin-allopen")

    configure<AllOpenExtension> {
        annotation("com.natchuz.hub.utils.Mockable")
    }

    dependencies {
        testImplementation(Deps.MOCKITO_KOTLIN)
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.suppressWarnings = true
            kotlinOptions.jvmTarget = "1.8"
        }

        withType<ShadowJar> {
            minimize()

            /* this excludes module-info.class present in Kotlin standard libraries, that causes Sponge to crash */
            exclude("META-INF/versions/**/*")
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

    tasks.withType<ShadowJar> {
        dependencies {
            exclude(dependency(Deps.SPONGE_API))
        }
    }
}

/**
 * Docker configuration
 */
projects("proxy", "core", "lobby", "backend:state") {
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