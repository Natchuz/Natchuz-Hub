package com.natchuz.hub.core.service

import com.natchuz.hub.backend.state.PlayerFlags
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import java.util.*

class NetworkPlayerService(
        val client: HttpClient = HttpClient {
            installJson()
        }
) : PlayerService {
    override fun get(player: UUID): NetworkPlayer = DefaultNetworkPlayer(player, client)
}

internal fun HttpClientConfig<*>.installJson() {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

class DefaultNetworkPlayer(val player: UUID, private val client: HttpClient) : NetworkPlayer {
    override val flags: List<PlayerFlags>
        get() {
            return runBlocking { client.get("http://state/player/$player/flags") }
        }
}