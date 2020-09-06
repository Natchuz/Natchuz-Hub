dependencies {
    implementation(Deps.Ktor.Client.ENGINE_CIO)
    implementation(Deps.Ktor.Client.JSON_SUPPORT)
    implementation(Deps.Ktor.Client.KOTLINX_SERIALIZATION)

    testImplementation(Deps.Ktor.Client.ENGINE_TEST)
}