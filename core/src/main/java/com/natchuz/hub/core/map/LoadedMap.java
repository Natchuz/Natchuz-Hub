package com.natchuz.hub.core.map;

import org.bukkit.World;

/**
 * Represents the loaded map, the world, its config and manifest
 */
public class LoadedMap {

    private final World world;
    private final String mapConfiguration;
    private final MapManifest manifest;

    public LoadedMap(World world, String mapConfiguration, MapManifest manifest) {
        this.world = world;
        this.mapConfiguration = mapConfiguration;
        this.manifest = manifest;
    }

    public World getWorld() {
        return world;
    }

    public String getMapConfiguration() {
        return mapConfiguration;
    }

    public MapManifest getManifest() {
        return manifest;
    }
}
