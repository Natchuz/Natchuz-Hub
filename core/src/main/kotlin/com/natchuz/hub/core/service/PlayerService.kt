package com.natchuz.hub.core.service

import com.natchuz.hub.backend.state.PlayerFlags
import org.spongepowered.api.entity.living.player.Player
import java.util.*

/** Client for state microservice for player */
interface PlayerService {
    operator fun get(player: UUID): NetworkPlayer
}

operator fun PlayerService.get(player: Player) = this[player.uniqueId]

interface NetworkPlayer {
    /** Get current player flags */
    val flags: List<PlayerFlags>
}
