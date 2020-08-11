import de.undercouch.gradle.tasks.download.Download

/*
    This file requires a significant cleanup
 */

plugins {
    id("de.undercouch.download")
}

configurations {
    create("localServer")
}

docker {
    name = "natchuz-hub-core"
}

val localServerDir = "$buildDir/localServer"
val localServerGroup = "build"

tasks {
    register("downloadPlugins", Download::class.java) {
        //NOTE: missing Worldedit
        src("https://github.com/CrushedPixel/PacketGate/releases/download/0.1.1/PacketGate-0.1.1.jar")
        dest("$localServerDir/mods/packetgate.jar")
        onlyIfNewer(true)
        overwrite(true)
        quiet(true)
        group = localServerGroup
    }

    register("downloadSponge", Download::class.java) {
        src("https://repo.spongepowered.org/maven/org/spongepowered/spongevanilla/1.12.2-7.2.3/spongevanilla-1.12.2-7.2.3.jar")
        dest(File("$localServerDir/sponge.jar"))
        onlyIfNewer(true)
        overwrite(true)
        quiet(true)
        group = localServerGroup
    }

    register("prepareLocalServer", Copy::class.java) {
        from("$buildDir/libs/core-all.jar") {
            into("mods")
        }
        from("${project(":sponge").buildDir}/libs/sponge-all.jar") {
            into("mods")
        }
        from("common")
        from("local")
        into(localServerDir)
        dependsOn("build")
        dependsOn("downloadSponge")
        dependsOn("downloadPlugins")
        dependsOn(":sponge:build")
        group = localServerGroup
        description = "Prepares local server model"
    }

    register("cleanLocalServer", Delete::class.java) {
        delete = setOf(localServerDir)
        group = localServerGroup
    }

    findByName("clean")?.dependsOn("cleanLocalServer")

    findByName("dockerPrepare")?.run {
        doFirst {
            copy {
                from("${project(":sponge").buildDir}/libs/sponge-all.jar")
                into("$buildDir/libs/sponge-all.jar")
            }
        }
        dependsOn(":sponge:build")
    }
}

dependencies {
    api(project(":sponge"))
    api(project(":protocol"))
    api(project(":utils"))

    implementation(Deps.SPONGE_API)

    implementation(Deps.COMMONS_IO)
    implementation(Deps.JAVA_MOJANG_API)

    "localServer"(files(localServerDir) {
        builtBy("prepareLocalServer")
    })
}