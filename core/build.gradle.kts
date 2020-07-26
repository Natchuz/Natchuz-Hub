import Vars.LOCAL_SERVER_CONFIGURATION
import de.undercouch.gradle.tasks.download.Download

plugins {
    id("de.undercouch.download")
}

configurations {
    create(LOCAL_SERVER_CONFIGURATION)
}

docker {
    name = "network"
}

val localServerDir = "$buildDir/localServer"
val localServerGroup = "build"

tasks.register("downloadPlugins", Download::class.java) {
    src(listOf(
            "https://ci.dmulloy2.net/job/ProtocolLib/440/artifact/target/ProtocolLib.jar",
            "https://media.forgecdn.net/files/2760/373/worldedit-bukkit-7.0.1.jar"
    ))
    dest("$localServerDir/plugins")
    onlyIfNewer(true)
    overwrite(true)
    quiet(true)
    group = localServerGroup
}

tasks.register("downloadPaper", Download::class.java) {
    src("https://papermc.io/api/v1/paper/1.14.4/latest/download")
    dest(File("$localServerDir/paper.jar"))
    onlyIfNewer(true)
    overwrite(true)
    quiet(true)
    group = localServerGroup
}

tasks.register("prepareLocalServer", Copy::class.java) {
    from("$buildDir/libs/core-all.jar") {
        into("plugins")
    }
    from("common")
    from("local")
    into(localServerDir)
    dependsOn("build")
    dependsOn("downloadPaper")
    dependsOn("downloadPlugins")
    group = localServerGroup
    description = "Prepares local server model"
}

dependencies {
    api(project(":paper"))
    api(project(":protocol"))
    api(project(":utils"))

    api(Deps.PAPER)
    api(Deps.PROTOCOL_LIB)

    compileOnly(Deps.PLUGIN_ANNOTATIONS)
    annotationProcessor(Deps.PLUGIN_ANNOTATIONS)

    implementation(Deps.COMMONS_IO)
    implementation(Deps.JAVA_MOJANG_API)

    LOCAL_SERVER_CONFIGURATION(files(localServerDir) {
        builtBy("prepareLocalServer")
    })
}