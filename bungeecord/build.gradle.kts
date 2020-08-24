dependencies {
    implementation(project(":protocol"))
    implementation(project(":utils"))
    implementation(project(":backend:state"))

    implementation(Deps.KTOR_CLIENT_CIO)
    implementation(Deps.KTOR_CLIENT_JSON)
    implementation(Deps.KTOR_CLIENT_SERIALIZATION)

    compileOnly(Deps.BUNGEECORD)

    testImplementation(Deps.BUNGEECORD)
    testImplementation(Deps.KTOR_CLIENT_TEST)
}

docker {
    name = "bungeecord"
}
