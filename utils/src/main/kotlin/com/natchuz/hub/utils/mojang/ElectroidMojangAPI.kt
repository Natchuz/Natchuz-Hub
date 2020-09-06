@file:UseSerializers(UUIDSerializer::class)

package com.natchuz.hub.utils.mojang

import com.natchuz.hub.utils.UUIDSerializer
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*
import kotlinx.serialization.json.Json as JsonBuilder

/**
 * Wrapper around Electroid Mojang API that cache data, and does not limit requests per minute
 *
 * @see [https://github.com/Electroid/mojang-api](https://github.com/Electroid/mojang-api)
 */
class ElectroidMojangAPI(
        val apiUrl: String = "https://api.ashcon.app/mojang/v2/user/%s",
        val client: HttpClient = HttpClient {
            installJson()
        },
) : MojangAPI {

    override fun getUsername(uuid: UUID): String? = runBlocking {
        getUser(uuid)?.username
    }

    override fun getUUID(username: String): UUID? = runBlocking {
        getUser(username)?.uuid
    }

    private suspend fun getUser(uuid: UUID): MojangUser? = try {
        client.get(apiUrl.format(uuid.toString()))
    } catch (_: ClientRequestException) {
        null
    }

    private suspend fun getUser(id: String): MojangUser? = try {
        client.get(apiUrl.format(id))
    } catch (_: ClientRequestException) {
        null
    }

    @Serializable
    private data class MojangUser(
            val uuid: UUID,
            val username: String,
    )
}

internal fun HttpClientConfig<*>.installJson() {
    install(JsonFeature) {
        serializer = KotlinxSerializer(JsonBuilder {
            ignoreUnknownKeys = true
        })
    }
}