dependencies {
    api(Deps.JEDIS)

    implementation(Deps.RABBITMQ)
    implementation(Deps.GUAVA)
    implementation(Deps.COMMONS_LANG)

    implementation(project(":utils"))
}