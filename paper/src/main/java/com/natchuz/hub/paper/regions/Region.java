package com.natchuz.hub.paper.regions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public abstract class Region {

    public boolean contains(Location point) {
        return this.contains(point.toVector());
    }

    public boolean contains(Block block) {
        return this.contains(BlockVectors.center(block));
    }

    public boolean contains(Entity entity) {
        return this.contains(entity.getLocation().toVector());
    }

    public boolean enters(Location from, Location to) {
        return !this.contains(from) && this.contains(to);
    }

    public boolean enters(Vector from, Vector to) {
        return !this.contains(from) && this.contains(to);
    }

    public boolean exits(Location from, Location to) {
        return this.contains(from) && !this.contains(to);
    }

    public boolean exits(Vector from, Vector to) {
        return this.contains(from) && !this.contains(to);
    }

    /**
     * Checks if Vector is in region
     */
    public abstract boolean contains(Vector loc);
}
