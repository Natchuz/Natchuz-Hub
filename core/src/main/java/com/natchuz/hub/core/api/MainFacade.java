package com.natchuz.hub.core.api;

import java.util.UUID;

import com.natchuz.hub.core.map.MapManifest;

/**
 * Ships all utils for all gamemodes
 */
public interface MainFacade {

    /**
     * Returns current's {@link MapManifest map manifest}
     */
    MapManifest getMapManifest();

    /**
     * Returns current's map configuration in raw {@link String} to parse it by yourself
     */
    String getMapConfiguration();

    /**
     * Returns UUID of current's map {@link org.spongepowered.api.world.World world}
     */
    UUID getMapWorld();

    /**
     * Server id
     */
    String getServerId();
}
