plugins {
    application
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(Deps.KTOR_SERVER_CORE)
    implementation(Deps.KTOR_SERVER_NETTY)
    implementation(Deps.KTOR_SERIALIZATION)
    implementation(Deps.SLF4J)
    implementation(project(":utils"))
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