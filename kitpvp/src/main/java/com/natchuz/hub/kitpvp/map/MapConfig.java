package com.natchuz.hub.kitpvp.map;

import lombok.Value;
import org.bukkit.Location;

import java.util.List;

/**
 * Represents map config
 */
@Value
public class MapConfig {
    Location hubNpcLocation;
    Location lobbySpawn;
    int lobbyVoidHeight;
    int arenaVoidHeight;
    int worldTime;
    List<KitLocation> kitPlaceholders;
    List<ArenaSpawn> arenaSpawns;
    List<Location> kitsClickInformation;

    @Value
    public static class KitLocation {
        String id;
        Location location;
    }
}
