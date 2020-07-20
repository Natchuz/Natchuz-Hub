package com.natchuz.hub.kitpvp.impl;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

import com.natchuz.hub.kitpvp.map.ArenaSpawn;
import com.natchuz.hub.kitpvp.map.MapConfig;

/**
 * Represents Arena area
 */
@ToString
public class Arena {

    @Getter
    private int voidHeight;
    @Getter
    private List<ArenaSpawn> arenaSpawns;

    private Arena() {
    }

    public static Arena createArena(MapConfig mapConfig) {
        Arena arena = new Arena();
        arena.arenaSpawns = mapConfig.getArenaSpawns();
        arena.voidHeight = mapConfig.getArenaVoidHeight();
        return arena;
    }
}
