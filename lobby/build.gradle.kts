import com.natchuz.hub.build.gamemode.GamemodeExtension
import com.natchuz.hub.build.gamemode.GamemodePlugin

apply<GamemodePlugin>()

configure<GamemodeExtension> {
    baseProject = project(":core")
}

docker {
    name = "lobby"
}

dependencies {
    implementation(Deps.SPONGE_API)
}

tasks.named("docker") {
    dependsOn(":core:docker")
}