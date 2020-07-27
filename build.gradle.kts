import com.palantir.gradle.docker.DockerExtension
import com.palantir.gradle.docker.PalantirDockerPlugin
import net.nemerosa.versioning.VersioningExtension
import net.nemerosa.versioning.VersioningPlugin
import net.nemerosa.versioning.tasks.VersionFileTask

plugins {
    id("net.nemerosa.versioning") version "2.8.2" apply false
    id("com.palantir.docker") version "0.22.1" apply false
    id("com.github.johnrengelman.shadow") version "5.2.0" apply false
    id("org.spongepowered.plugin") version "0.9.0"
    kotlin("jvm") version "1.3.72" apply false
    idea
    `java-library`
}

//region java projects configuration

repositories {
    mavenCentral()
    jcenter()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://jitpack.io")
    maven("https://papermc.io/repo/repository/maven-public/")
}

val javaProjects = allprojects

javaProjects.forEach { p ->
    p.apply(plugin = "com.github.johnrengelman.shadow")
    p.apply(plugin = "org.gradle.java-library")
    p.apply<VersioningPlugin>()

    p.dependencies {
        compileOnly(Deps.LOMBOK)
        annotationProcessor(Deps.LOMBOK)

        testImplementation(Deps.MOCKITO)
        testImplementation(Deps.JUPITER_API)
        testImplementation(Deps.JUPITER_PARAMS)
        testRuntimeOnly(Deps.JUPITER_ENGINE)
    }

    p.configure<VersioningExtension> {
        dirtySuffix = "-custom"
        noWarningOnDirty = true
    }

    p.tasks {
        named<VersionFileTask>("versionFile") {
            file = File(project.buildDir, "resources/main/version.properties")
        }

        withType<JavaCompile>().forEach {
            it.options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:deprecation"))
        }

        named("assemble") {
            dependsOn("shadowJar")
        }

        named<Test>("test") {
            useJUnitPlatform()
        }

        named("shadowJar") {
            dependsOn("jar")
        }

        named("jar") {
            dependsOn("versionFile")

        }
    }
}

//endregion
//region dockerized projects configurations

val dockerizedProjects = arrayOf(
        project("bungeecord"),
        project("service"),
        project("core"),
        project("kitpvp"),
        project("lobby")
)

dockerizedProjects.forEach { p ->
    p.apply<PalantirDockerPlugin>()

    p.configure<DockerExtension> {

        // Idk how it works, but I guess this and .dockerignore interpolates themselves
        copySpec.into(".") {
            from(".") {
                exclude("build/", "src/")
            }

            from(".") {
                include("build/libs/*")
            }
        }

    }

    p.tasks {
        named("docker") {
            dependsOn("build")
        }

        named("dockerPrepare") {
            dependsOn("shadowJar")
        }
    }
}

//endregion