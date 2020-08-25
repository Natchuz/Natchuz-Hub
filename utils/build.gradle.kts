dependencies {
    api(Deps.GUAVA)
    api(Deps.COMMONS_LANG)
    api(Deps.JAVA_TUPLES)
    api(Deps.UNIREST)
    implementation(Deps.KOTLIN_SERIALIZATION_CORE)
    testImplementation(Deps.KTOR_CLIENT_TEST)
    implementation(Deps.KTOR_CLIENT_CIO)
    implementation(Deps.KTOR_CLIENT_JSON)
    implementation(Deps.KTOR_CLIENT_SERIALIZATION)
}