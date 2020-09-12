package com.natchuz.hub.core.context

import com.natchuz.hub.core.map.LocalMapRepository
import com.natchuz.hub.core.map.MapRepository
import com.natchuz.hub.core.modules.FriendsNotifier
import com.natchuz.hub.core.modules.Module
import com.natchuz.hub.core.modules.PlayerSubscriber
import com.natchuz.hub.core.proxy.NetworkProxyBackend
import com.natchuz.hub.core.proxy.ProxyBackend
import com.natchuz.hub.core.user.NetworkUserService
import com.natchuz.hub.core.user.UserService
import com.natchuz.hub.protocol.arch.Services
import com.natchuz.hub.protocol.messaging.Protocol
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.spongepowered.api.Game
import java.io.File

/**
 * Represents context of running in full network
 */
class NetworkContext(
        private val protocol: Protocol = Protocol(Services.PAPER.createClient()),
        private val httpClient: HttpClient = HttpClient {
            installJson()
        },
        private val game: Game
) : ServerContext {

    override fun createMapRepository(): MapRepository {
        val repo = File(game.gameDirectory.toFile().absoluteFile.toString() + "/maps")
        repo.mkdirs()
        return LocalMapRepository(repo)
    }

    override fun createProxyBackend(): ProxyBackend = NetworkProxyBackend(httpClient)

    override fun createUserService(): UserService = NetworkUserService(httpClient)

    override fun createModules(): List<Module> = listOf(
            FriendsNotifier(protocol),
            PlayerSubscriber(protocol),
    )
}

internal fun HttpClientConfig<*>.installJson() {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}