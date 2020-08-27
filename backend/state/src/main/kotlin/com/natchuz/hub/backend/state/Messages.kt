@file:UseSerializers(UUIDSerializer::class)

package com.natchuz.hub.backend.state

import com.natchuz.hub.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus

/** Contains information about if player was logged */
@Serializable
sealed class PlayerLoginStatus(val status: String) {

    /** Serializer for [PlayerLoginStatus] */
    companion object Serializer : JsonContentPolymorphicSerializer<PlayerLoginStatus>(PlayerLoginStatus::class) {
        override fun selectDeserializer(element: JsonElement) = when (element.jsonObject["status"]?.jsonPrimitive?.content) {
            "ok" -> Ok.serializer()
            "ban" -> Ban.serializer()
            "other" -> Other.serializer()
            "maintenance" -> Maintenance.serializer()
            else -> throw IllegalStateException("Wrong status ID")
        }
    }

    /** Player was logged in */
    @Serializable
    data class Ok(
            /** Target server id */
            val targetServer: String
    ) : PlayerLoginStatus("ok")

    /** Player could not be logged in due to ban */
    @Serializable
    data class Ban(
            /** Ban reason */
            val reason: String
    ) : PlayerLoginStatus("ban")

    /** Player could not be logged due to other reasons */
    @Serializable
    data class Other(
            /** Reason info */
            val info: String
    ) : PlayerLoginStatus("other")

    /** Player could not be logged due to pending maintenance */
    @Serializable
    class Maintenance : PlayerLoginStatus("maintenance")
}

@Serializable
data class SendRequest(
        val targetServer: String,
        val flags: List<PlayerFlags>
)

/**
 * [Json] containing modules for serializing classes in this file
 */
val MessagesJson = Json {
    prettyPrint = true
    serializersModule += SerializersModule {
        contextual(PlayerLoginStatus::class, PlayerLoginStatus.Serializer)
    }
}