import com.natchuz.hub.build.gamemode.GamemodePlugin

apply<GamemodePlugin>()

docker {
    name = "kitpvp"
}

tasks.named("docker") {
    dependsOn(":core:docker")
}