dependencies {
    implementation(project(":protocol"))
    implementation(project(":utils"))

    compileOnly(Deps.BUNGEECORD)
    testImplementation(Deps.BUNGEECORD)
}

docker {
    name = "bungeecord"
}
