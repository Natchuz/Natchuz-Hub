@file:UseSerializers(UUIDSerializer::class)

package com.natchuz.hub.backend.state

import com.natchuz.hub.utils.UUIDSerializer
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.*

/** Use to acknowledge if player can be logged */
@Serializable
data class PlayerLoginRequest(
        /** UUID of player */
        val player: UUID
)

/** Response to [PlayerLoginRequest] */
@Serializable
sealed class PlayerLoginResponse(val status: String) {

    companion object Serializer : JsonContentPolymorphicSerializer<PlayerLoginResponse>(PlayerLoginResponse::class) {
        override fun selectDeserializer(element: JsonElement) = when (element.jsonObject["status"]?.jsonPrimitive?.content) {
            "ok" -> OkStatus.serializer()
            "ban" -> BanStatus.serializer()
            "other" -> OtherStatus.serializer()
            "maintenance" -> MaintenanceStatus.serializer()
            "error" -> ErrorStatus.serializer()
            else -> throw IllegalStateException("Wrong status ID")
        }
    }

    /** Indicates that player can log in */
    @Serializable
    data class OkStatus(
            /** Target server id */
            val targetServer: String
    ) : PlayerLoginResponse("ok")

    /** When player is banned */
    @Serializable
    data class BanStatus(val reason: String) : PlayerLoginResponse("ban")

    @Serializable
    data class ErrorStatus(val info: String) : PlayerLoginResponse("error")

    @Serializable
    data class OtherStatus(val other: String) : PlayerLoginResponse("other")

    @Serializable
    object MaintenanceStatus : PlayerLoginResponse("maintenance")
}

@Serializable
data class PlayerLogoutRequest(
        /** UUID of player */
        val player: UUID
)