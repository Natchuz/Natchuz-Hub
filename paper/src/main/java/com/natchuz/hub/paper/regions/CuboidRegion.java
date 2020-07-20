package com.natchuz.hub.paper.regions;

import org.apache.commons.lang.Validate;
import org.bukkit.util.Vector;

import java.util.Objects;

/**
 * Represents cuboid region
 */
public class CuboidRegion extends Region {

    private final Vector min;
    private final Vector max;

    /**
     * Create cuboid based on 2 vertices
     */
    public CuboidRegion(Vector min, Vector max) {
        Validate.notNull(min);
        Validate.notNull(max);
        this.min = min.clone();
        this.max = max.clone();
    }

    /**
     * Clone other cuboid
     */
    public CuboidRegion(CuboidRegion region) {
        Validate.notNull(region);
        this.max = region.getMax().clone();
        this.min = region.getMin().clone();
    }

    /**
     * Create cuboid based on 2 vertices
     */
    public CuboidRegion(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
        this(new Vector(minx, miny, minz), new Vector(maxx, maxy, maxz));
    }

    @Override
    public boolean contains(Vector loc) {
        Validate.notNull(loc);
        return loc.isInAABB(min, max);
    }

    public Vector getMin() {
        return min;
    }

    public Vector getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "CuboidRegion{" + "min=" + min + ", max=" + max + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CuboidRegion region = (CuboidRegion) o;
        return min.equals(region.min) && max.equals(region.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }
}
