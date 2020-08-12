package com.natchuz.hub.lobby

import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

/**
 * Lobby map configuration
 */
data class MapConfig(
        /**
         * Get location of rewards npc
         */
        val rewards: Location<World>,
        /**
         * Get location of kitpvp npc
         */
        val kitpvp: Location<World>,
        /**
         * Get spawn location
         */
        val spawn: Location<World>,
        /**
         * Height on which player will teleport on spawn if fall from map
         */
        val height: Int
)

