@file:UseSerializers(UUIDSerializer::class)

package com.natchuz.hub.core.user

import com.natchuz.hub.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

enum class UserRank {
    DEFAULT,
    VIP,
    PATRON,
    MODERATOR,
    ADMIN;
}

@Serializable
data class User(
        val uuid: UUID,
        val rank: UserRank,
        val friends: List<UUID>,
        val invitedFriends: List<UUID>,
        val pendingFriends: List<UUID>,
)