package com.natchuz.hub.core.user

import com.natchuz.hub.backend.state.PlayerFlags
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

    override fun getState(uuid: UUID): NetworkPlayer = DefaultStandalonePlayer()
}

class DefaultStandalonePlayer : NetworkPlayer {
    override val flags: List<PlayerFlags> = listOf(PlayerFlags.PROXY_JOIN)
}