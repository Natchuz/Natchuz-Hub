package com.natchuz.hub.core.service

import com.natchuz.hub.backend.state.PlayerFlags
import java.util.*

class StandalonePlayerService : PlayerService {
    override fun get(player: UUID): NetworkPlayer = DefaultStandalonePlayer()
}

class DefaultStandalonePlayer : NetworkPlayer {
    override val flags: List<PlayerFlags> = listOf(PlayerFlags.PROXY_JOIN)
}