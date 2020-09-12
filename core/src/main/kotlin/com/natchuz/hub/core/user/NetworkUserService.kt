package com.natchuz.hub.core.user

import com.natchuz.hub.backend.state.PlayerFlags
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.util.*

class NetworkUserService(
        private val client: HttpClient
) : UserService {

    override fun getUser(uuid: UUID): User = runBlocking {
        client.get(
                url = Url(playerEndpoint(uuid))
        )
    }

    override fun getState(uuid: UUID): NetworkPlayer = DefaultNetworkPlayer(uuid, client)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun playerEndpoint(uuid: UUID) = "http://users/$uuid"
}

class DefaultNetworkPlayer(val player: UUID, private val client: HttpClient) : NetworkPlayer {
    override val flags: List<PlayerFlags>
        get() {
            return runBlocking { client.get("http://state/player/$player/flags") }
        }
}