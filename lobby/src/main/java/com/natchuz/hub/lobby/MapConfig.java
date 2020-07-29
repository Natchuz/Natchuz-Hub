package com.natchuz.hub.lobby;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Lobby map configuration
 */
public class MapConfig {

    private final Location<World> rewards;
    private final Location<World> kitpvp;
    private final Location<World> spawn;

    public MapConfig(Location<World> rewards, Location<World> kitpvp, Location<World> spawn) {
        this.rewards = rewards;
        this.kitpvp = kitpvp;
        this.spawn = spawn;
    }

    /**
     * Get location of rewards npc
     */
    public Location<World> getRewards() {
        return rewards;
    }

    /**
     * Get location of kitpvp npc
     */
    public Location<World> getKitpvp() {
        return kitpvp;
    }

    /**
     * Get spawn location
     */
    public Location<World> getSpawn() {
        return spawn;
    }

    @Override
    public String toString() {
        return "MapConfig{" + "rewards=" + rewards + ", kitpvp=" + kitpvp + '}';
    }
}
