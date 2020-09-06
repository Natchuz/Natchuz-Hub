import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":utils"))
    implementation(Deps.PACKET_GATE)
}

tasks.withType<ShadowJar> {
    dependencies {
        exclude(dependency(Deps.PACKET_GATE))
    }
}