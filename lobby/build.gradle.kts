import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.natchuz.hub.build.gamemode.GamemodeExtension
import com.natchuz.hub.build.gamemode.GamemodePlugin

apply<GamemodePlugin>()

configure<GamemodeExtension> {
    baseProject = project(":core:base")
}

docker {
    name = "lobby"
}

dependencies {
    implementation(project(":core:api"))
    implementation(project(":utils"))
    implementation(project(":sponge"))
}

tasks {
    named("docker") {
        dependsOn(":core:docker")
    }

    withType<ShadowJar> {
        dependencies {
            exclude(project(":core"))
        }
    }
}