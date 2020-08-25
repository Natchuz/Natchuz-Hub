package com.natchuz.hub.sponge.regions;

import com.flowpowered.math.vector.Vector3d;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * Represents cylinder region
 */
public class CylinderRegion extends Region {

    private final Vector3d base;
    private final double height;
    private final double radius;
    private final double radiusSq;

    /**
     * Creates cylinder region
     *
     * @param base   Vector3d as base of cylinder (center of lower base)
     * @param height height
     * @param radius radius
     * @throws IllegalArgumentException when radius isn't greater that 0
     */
    public CylinderRegion(Vector3d base, double height, double radius) {
        Validate.notNull(base);
        Validate.isTrue(radius > 0);

        this.base = base.clone();
        this.height = height;
        this.radius = radius;
        this.radiusSq = radius * radius;
    }

    /**
     * Clone other cylinder region
     */
    public CylinderRegion(CylinderRegion region) {
        Validate.notNull(region);

        this.base = region.base.clone();
        this.height = region.height;
        this.radius = region.radius;
        this.radiusSq = region.radiusSq;
    }

    @Override
    public boolean contains(Vector3d loc) {
        return loc.getY() >= this.base.getY()
                && loc.getY() <= (this.base.getY() + this.height)
                && Math.pow(loc.getX() - this.base.getX(), 2)
                + Math.pow(loc.getZ() - this.base.getZ(), 2)
                <= this.radiusSq;
    }

    public Vector3d getBase() {
        return base;
    }

    public double getHeight() {
        return height;
    }

    public double getRadius() {
        return radius;
    }

    public double getRadiusSq() {
        return radiusSq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CylinderRegion that = (CylinderRegion) o;
        return Double.compare(that.height, height) == 0 && Double.compare(that.radius, radius) == 0 && base.equals(that.base);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, height, radius);
    }

    @Override
    public String toString() {
        return "CylinderRegion{" + "base=" + base + ", height=" + height + ", radius=" + radius + ", radiusSq=" + radiusSq + '}';
    }
}
