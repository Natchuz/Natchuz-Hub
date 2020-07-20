import com.natchuz.hub.build.gamemode.GamemodeExtension
import com.natchuz.hub.build.gamemode.GamemodePlugin

apply<GamemodePlugin>()

configure<GamemodeExtension> {
    baseProject = project(":core")
}

docker {
    name = "lobby"
}

tasks.named("docker") {
    dependsOn(":core:docker")
}