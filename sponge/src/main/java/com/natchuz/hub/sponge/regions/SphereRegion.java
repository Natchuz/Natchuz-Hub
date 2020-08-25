package com.natchuz.hub.sponge.regions;

import com.flowpowered.math.vector.Vector3d;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * Represents sphere region
 */
public class SphereRegion extends Region {

    private final Vector3d center;
    private final double radius;
    private final double radiusSquared;

    /**
     * Create new sphere region
     *
     * @param center center of sphere
     * @param radius radius
     * @throws IllegalArgumentException when radius isn't greater than 0
     */
    public SphereRegion(Vector3d center, double radius) {
        Validate.notNull(center, "Sphere center cannot be null!");
        Validate.isTrue(radius > 0, "Radius must be bigger than 0!");

        this.center = center.clone();
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    /**
     * Clone other sphere
     */
    public SphereRegion(SphereRegion region) {
        Validate.notNull(region);

        this.center = region.center.clone();
        this.radius = region.radius;
        this.radiusSquared = region.radiusSquared;
    }

    @Override
    public boolean contains(Vector3d loc) {
        return center.distanceSquared(loc) <= radius * radius;
    }

    public Vector3d getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public double getRadiusSquared() {
        return radiusSquared;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SphereRegion that = (SphereRegion) o;
        return Double.compare(that.radius, radius) == 0 && Double.compare(that.radiusSquared, radiusSquared) == 0 && center.equals(that.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, radius, radiusSquared);
    }

    @Override
    public String toString() {
        return "SphereRegion{" + "center=" + center + ", radius=" + radius + ", radiusSquared=" + radiusSquared + '}';
    }
}
