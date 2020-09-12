package com.natchuz.hub.core.api.user

import com.natchuz.hub.backend.state.PlayerFlags
import java.util.*

interface UserService {
    fun getUser(uuid: UUID): User

    fun getState(uuid: UUID): NetworkPlayer
}

interface NetworkPlayer {
    /** Get current player flags */
    val flags: List<PlayerFlags>
}
