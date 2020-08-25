package com.natchuz.hub.sponge.regions;

import com.flowpowered.math.vector.Vector3d;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * Represents cuboid region
 */
public class CuboidRegion extends Region {

    private final Vector3d min;
    private final Vector3d max;

    /**
     * Create cuboid based on 2 vertices
     */
    public CuboidRegion(Vector3d min, Vector3d max) {
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
        this(new Vector3d(minx, miny, minz), new Vector3d(maxx, maxy, maxz));
    }

    @Override
    public boolean contains(Vector3d loc) {
        Validate.notNull(loc);
        return loc.getX() >= min.getX() && loc.getX() <= max.getX() &&
                loc.getY() >= min.getY() && loc.getY() <= max.getY() &&
                loc.getZ() >= min.getZ() && loc.getZ() <= max.getZ();
    }

    public Vector3d getMin() {
        return min;
    }

    public Vector3d getMax() {
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
