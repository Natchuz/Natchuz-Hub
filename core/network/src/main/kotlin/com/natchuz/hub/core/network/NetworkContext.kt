package com.natchuz.hub.core.network

import com.natchuz.hub.core.base.map.LocalMapRepository
import com.natchuz.hub.core.base.map.MapRepository
import com.natchuz.hub.core.api.proxy.ProxyBackend
import com.natchuz.hub.core.api.user.UserService
import com.natchuz.hub.core.base.context.Module
import com.natchuz.hub.core.base.context.ServerContext
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.spongepowered.api.Game
import java.io.File

/**
 * Represents context of running in full network
 */
class NetworkContext @JvmOverloads constructor(
        private val httpClient: HttpClient = HttpClient {
            installJson()
        },
        private val game: Game,
) : ServerContext {

    override fun createMapRepository(): MapRepository {
        val repo = File(game.gameDirectory.toFile().absoluteFile.toString() + "/maps")
        repo.mkdirs()
        return LocalMapRepository(repo)
    }

    override fun createProxyBackend(): ProxyBackend = NetworkProxyBackend(httpClient)

    override fun createUserService(): UserService = NetworkUserService(httpClient)

    override fun createModules(): List<Module> = emptyList()
}

internal fun HttpClientConfig<*>.installJson() {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}