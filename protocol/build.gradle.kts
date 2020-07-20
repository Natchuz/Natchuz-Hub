dependencies {
    api(project(":utils"))

    api(Deps.JEDIS)
    api(Deps.MONGO_SYNC)

    implementation(Deps.RABBITMQ)
}