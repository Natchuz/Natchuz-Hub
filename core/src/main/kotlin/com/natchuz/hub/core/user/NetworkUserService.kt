package com.natchuz.hub.core.user

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

    @Suppress("NOTHING_TO_INLINE")
    private inline fun playerEndpoint(uuid: UUID) = "http://users/$uuid"
}