import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
}

dependencies {
    implementation(project(":utils"))
    implementation(project(":protocol"))

    implementation(Deps.Ktor.Server.ENGINE_NETTY)
    implementation(Deps.Ktor.Server.SERIALIZATION)
    implementation(Deps.SLF4J)
    implementation(Deps.MONGO_SYNC)

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

    withType<ShadowJar> {
        minimize {
            exclude(dependency(Deps.Ktor.Server.ENGINE_NETTY))
        }
    }
}

docker {
    name = "backend-users"
}