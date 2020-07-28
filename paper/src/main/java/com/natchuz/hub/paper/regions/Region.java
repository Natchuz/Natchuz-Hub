package com.natchuz.hub.paper.regions;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Location;

public abstract class Region {

    public boolean contains(Entity entity) {
        return contains(entity.getLocation().getPosition());
    }

    public boolean contains(Location<?> point) {
        return contains(point.getPosition());
    }

    public boolean containsBlock(Location<?> blockPos) {
        return contains(blockPos.getBlockPosition().toDouble());
    }

    public boolean enters(Location<?> from, Location<?> to) {
        return !contains(from) && contains(to);
    }

    public boolean enters(Vector3d from, Vector3d to) {
        return !contains(from) && contains(to);
    }

    public boolean exits(Location<?> from, Location<?> to) {
        return contains(from) && !contains(to);
    }

    public boolean exits(Vector3d from, Vector3d to) {
        return contains(from) && !contains(to);
    }

    /**
     * Checks if Vector is in region
     */
    public abstract boolean contains(Vector3d loc);
}
