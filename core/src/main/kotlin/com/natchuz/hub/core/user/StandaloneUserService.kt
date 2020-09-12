package com.natchuz.hub.core.user

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.util.*

class StandaloneUserService : UserService {

    private val users: Map<UUID, User> = mutableMapOf()

    override fun getUser(uuid: UUID): User = users[uuid] ?: User(
            uuid = uuid,
            rank = UserRank.DEFAULT,
            friends = mutableListOf(),
            invitedFriends = mutableListOf(),
            pendingFriends = mutableListOf(),
    )
}