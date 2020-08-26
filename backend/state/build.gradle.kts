plugins {
    application
}

dependencies {
    implementation(project(":utils"))

    implementation(Deps.Ktor.Server.CORE)
    implementation(Deps.Ktor.Server.ENGINE_NETTY)
    implementation(Deps.Ktor.Server.SERIALIZATION)
    implementation(Deps.SLF4J)
    implementation(Deps.JEDIS)

    testImplementation(Deps.Ktor.Server.ENGINE_TEST)
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks {
    withType<Jar> {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClassName))
        }
    }
}

docker {
    name = "backend-state"
}