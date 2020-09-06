/**
 * List of used dependencies to make cleaner gradle files
 */
object Deps {

    const val LOMBOK = "org.projectlombok:lombok:1.18.12"

    const val JUPITER_API = "org.junit.jupiter:junit-jupiter-api:5.6.0"
    const val JUPITER_PARAMS = "org.junit.jupiter:junit-jupiter-params:5.2.0"
    const val JUPITER_ENGINE = "org.junit.jupiter:junit-jupiter-engine:5.6.0"
    const val MOCKITO = "org.mockito:mockito-core:2.28.2"
    const val MOCKITO_KOTLIN = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"

    const val COMMONS_IO = "commons-io:commons-io:2.5"
    const val COMMONS_LANG = "commons-lang:commons-lang:2.6"

    const val GSON = "com.google.code.gson:gson:2.8.6"
    const val GUAVA = "com.google.guava:guava:29.0-jre"

    const val SLF4J = "org.slf4j:slf4j-simple:1.7.30"

    const val JEDIS = "redis.clients:jedis:3.1.0"
    const val MONGO_SYNC = "org.mongodb:mongodb-driver-sync:4.0.2"
    const val RABBITMQ = "com.rabbitmq:amqp-client:5.8.0"

    const val BUNGEECORD = "net.md-5:bungeecord-api:1.16-R0.2-SNAPSHOT"
    const val SPONGE_API = "org.spongepowered:spongeapi:7.2.0"
    const val PACKET_GATE = "com.github.CrushedPixel:PacketGate:0.1.1"

    object Ktor {
        private const val KTOR_VERSION = "1.4.0"

        object Client {
            const val ENGINE_CIO = "io.ktor:ktor-client-cio-jvm:$KTOR_VERSION"
            const val JSON_SUPPORT = "io.ktor:ktor-client-json-jvm:$KTOR_VERSION"
            const val KOTLINX_SERIALIZATION = "io.ktor:ktor-client-serialization-jvm:$KTOR_VERSION"
            const val ENGINE_TEST = "io.ktor:ktor-client-mock-jvm:$KTOR_VERSION"
        }

        object Server {
            const val CORE = "io.ktor:ktor-server-core:1.4.0"
            const val ENGINE_NETTY = "io.ktor:ktor-server-netty:1.4.0"
            const val ENGINE_TEST = "io.ktor:ktor-server-test-host:1.4.0"
            const val SERIALIZATION = "io.ktor:ktor-serialization:1.4.0"
            const val GSON = "io.ktor:ktor-gson:1.4.0"
            const val LOCATIONS = "io.ktor:ktor-locations:1.4.0"
        }
    }
}

/**
 * List of plugin identifiers
 */
object Plugins {
    const val VERSIONING = "net.nemerosa.versioning"
    const val DOCKER = "com.palantir.docker"
    const val SHADOW = "com.github.johnrengelman.shadow"
    const val SPONGE = "org.spongepowered.plugin"
}
