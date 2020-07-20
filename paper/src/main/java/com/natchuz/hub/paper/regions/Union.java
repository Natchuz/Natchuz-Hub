package com.natchuz.hub.paper.regions;

import org.apache.commons.lang.Validate;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Represents combined region
 */
public class Union extends Region implements Iterable<Region> {

    private final Region[] regions;

    /**
     * creates combined region
     *
     * @param regions regions to be combined
     */
    public Union(Region... regions) {
        Validate.noNullElements(regions);
        this.regions = regions;
    }

    @Override
    public boolean contains(Vector loc) {
        for (Region region : regions) {
            if (region.contains(loc)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Region> iterator() {
        return Arrays.stream(regions).iterator();
    }

    public Region[] getRegions() {
        return regions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Union union = (Union) o;
        return Arrays.equals(regions, union.regions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(regions);
    }

    @Override
    public String toString() {
        return "Union{" + "regions=" + Arrays.toString(regions) + '}';
    }
}
