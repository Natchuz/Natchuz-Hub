package com.natchuz.hub.lobby;

import org.bukkit.Location;

/**
 * Lobby map configuration
 */
public class MapConfig {

    private final Location rewards;
    private final Location kitpvp;
    private final Location spawn;

    public MapConfig(Location rewards, Location kitpvp, Location spawn) {
        this.rewards = rewards;
        this.kitpvp = kitpvp;
        this.spawn = spawn;
    }

    /**
     * Get location of rewards npc
     */
    public Location getRewards() {
        return rewards;
    }

    /**
     * Get location of kitpvp npc
     */
    public Location getKitpvp() {
        return kitpvp;
    }

    /**
     * Get spawn location
     */
    public Location getSpawn() {
        return spawn;
    }

    @Override
    public String toString() {
        return "MapConfig{" + "rewards=" + rewards + ", kitpvp=" + kitpvp + '}';
    }
}
