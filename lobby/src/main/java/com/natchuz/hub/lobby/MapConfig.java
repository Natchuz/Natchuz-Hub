package com.natchuz.hub.lobby;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Lobby map configuration
 */
public class MapConfig {

    private Location<World> rewards;
    private Location<World> kitpvp;
    private Location<World> spawn;
    private int height;

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

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "MapConfig{" + "rewards=" + rewards + ", kitpvp=" + kitpvp + '}';
    }
}
