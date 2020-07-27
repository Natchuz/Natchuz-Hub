/**
 * List of used dependencies to make cleaner gradle files
 */
object Deps {

    const val LOMBOK = "org.projectlombok:lombok:1.18.10"

    const val PAPER = "com.destroystokyo.paper:paper-api:1.14.4-R0.1-SNAPSHOT"
    const val PROTOCOL_LIB = "com.comphenix.protocol:ProtocolLib:4.5.1"
    const val PLUGIN_ANNOTATIONS = "org.spigotmc:plugin-annotations:1.2.2-SNAPSHOT"

    const val BUNGEECORD = "net.md-5:bungeecord-api:1.14-SNAPSHOT"

    const val MOCKITO = "org.mockito:mockito-core:2.28.2"
    const val JUPITER_API = "org.junit.jupiter:junit-jupiter-api:5.6.0"
    const val JUPITER_PARAMS = "org.junit.jupiter:junit-jupiter-params:5.2.0"
    const val JUPITER_ENGINE = "org.junit.jupiter:junit-jupiter-engine:5.6.0"

    const val COMMONS_IO = "commons-io:commons-io:2.5"
    const val JAVA_MOJANG_API = "com.github.SparklingComet:java-mojang-api:-SNAPSHOT"
    const val JSON = "org.json:json:20190722"
    const val REFLECTIONS = "org.reflections:reflections:0.9.11"
    const val COMMONS_LANG = "commons-lang:commons-lang:2.6"
    const val JAVA_TUPLES = "org.javatuples:javatuples:1.2"
    const val UNIREST = "com.konghq:unirest-java:3.4.01"
    const val GSON = "com.google.code.gson:gson:2.8.6"
    const val GUAVA = "com.google.guava:guava:29.0-jre"

    const val JEDIS = "redis.clients:jedis:3.1.0"
    const val MONGO_SYNC = "org.mongodb:mongodb-driver-sync:4.0.2"
    const val RABBITMQ = "com.rabbitmq:amqp-client:5.8.0"

    const val MOCKITO_KOTLIN = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"

    const val SPONGE_API = "org.spongepowered:spongeapi:7.2.0"
}

/**
 * Common variables
 */
object Vars {
    //const val LOCAL_SERVER_CONFIGURATION = "localServer"
}

/**
 * List of plugin identifiers
 */
object Plugins {
    const val VERSIONING = "net.nemerosa.versioning"
    const val DOCKER = "com.palantir.docker"
    const val SHADOW = "com.github.johnrengelman.shadow"
    const val SPONGE = "org.spongepowered.plugin"
    const val NMS = "net.minecrell.vanillagradle.server"
}
