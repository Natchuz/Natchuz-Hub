import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":protocol"))
    implementation(project(":utils"))
    implementation(project(":backend:state"))

    implementation(Deps.BUNGEECORD)
    implementation(Deps.Ktor.Client.ENGINE_CIO)
    implementation(Deps.Ktor.Client.JSON_SUPPORT)
    implementation(Deps.Ktor.Client.KOTLINX_SERIALIZATION)

    testImplementation(Deps.Ktor.Client.ENGINE_TEST)
}

docker {
    name = "bungeecord"
}

tasks.withType<ShadowJar> {
    dependencies {
        exclude(dependency(Deps.BUNGEECORD))
    }

    minimize {
        exclude(dependency(Deps.Ktor.Client.ENGINE_CIO))
    }
}