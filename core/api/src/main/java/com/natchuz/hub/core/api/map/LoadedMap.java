package com.natchuz.hub.core.api.map;

import java.util.UUID;

/**
 * Represents the loaded map, the world, its config and manifest
 */
public class LoadedMap {

    private final UUID world;
    private final String mapConfiguration;
    private final MapManifest manifest;

    public LoadedMap(UUID world, String mapConfiguration, MapManifest manifest) {
        this.world = world;
        this.mapConfiguration = mapConfiguration;
        this.manifest = manifest;
    }

    public UUID getWorld() {
        return world;
    }

    public String getMapConfiguration() {
        return mapConfiguration;
    }

    public MapManifest getManifest() {
        return manifest;
    }
}
