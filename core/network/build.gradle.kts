dependencies {
    implementation(project(":core:base"))
    implementation(project(":utils"))

    implementation(Deps.Ktor.Client.ENGINE_CIO)
    implementation(Deps.Ktor.Client.JSON_SUPPORT)
    implementation(Deps.Ktor.Client.KOTLINX_SERIALIZATION)
}