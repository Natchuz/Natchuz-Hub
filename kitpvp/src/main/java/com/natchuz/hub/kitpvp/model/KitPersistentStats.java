package com.natchuz.hub.kitpvp.model;

import lombok.Getter;

@Getter
public class KitPersistentStats {

    private int kills;
    private int deaths;
    private int bestStreak;

    public KitPersistentStats() {
        kills = 0;
        deaths = 0;
        bestStreak = 0;
    }

    public void addStats(ArenaStats stats) {
        deaths++;
        kills += stats.getKills();
        bestStreak = Math.max(bestStreak, stats.getBestStreaks());
    }
}
