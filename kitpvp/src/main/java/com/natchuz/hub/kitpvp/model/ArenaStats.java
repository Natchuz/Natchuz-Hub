package com.natchuz.hub.kitpvp.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ArenaStats {

    private int kills;
    private int bestStreaks;

    public void addKill() {
        this.kills++;
    }

    public void setBestStreaks(int bestStreaks) {
        this.bestStreaks = bestStreaks;
    }
}
