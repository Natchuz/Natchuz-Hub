package com.natchuz.paper.regions;

import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;

import com.natchuz.hub.paper.regions.CuboidRegion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CuboidRegionTest {

    @Test
    public void testContains() {
        Vector vectorMin = new Vector(1, 1, 1);
        Vector vectorMax = new Vector(4, 4, 4);

        CuboidRegion region = new CuboidRegion(vectorMin, vectorMax);

        assertTrue(region.contains(new Vector(2, 1, 3)));
        assertTrue(region.contains(new Vector(3, 3, 4)));
        assertTrue(region.contains(new Vector(3, 4, 1)));

        assertFalse(region.contains(new Vector(5, 4, 1)));
        assertFalse(region.contains(new Vector(3, 4.1, 1)));
        assertFalse(region.contains(new Vector(0, 4, 1)));
    }

    @Test
    public void testEnters() {
        Vector vectorMin = new Vector(1, 1, 1);
        Vector vectorMax = new Vector(4, 4, 4);

        CuboidRegion region = new CuboidRegion(vectorMin, vectorMax);

        assertTrue(region.enters(new Vector(0, 2, 2), new Vector(2, 2, 2)));
        assertTrue(region.enters(new Vector(4.1, 2, 2), new Vector(3, 2, 2)));
        assertTrue(region.enters(new Vector(5, 2, 2), new Vector(4, 2, 2)));

        assertFalse(region.enters(new Vector(-1, -1, -1), new Vector(-2, -2, -2)));
        assertFalse(region.enters(new Vector(0.1, 0.1, 0.1), new Vector(0, 1, 1.1)));
        assertFalse(region.enters(new Vector(3, 2, 1), new Vector(4, 1.1, 1)));
    }

    @Test
    public void testExit() {
        Vector vectorMin = new Vector(1, 1, 1);
        Vector vectorMax = new Vector(4, 4, 4);

        CuboidRegion region = new CuboidRegion(vectorMin, vectorMax);

        assertTrue(region.exits(new Vector(2, 2, 2), new Vector(0, 0, 0)));
        assertTrue(region.exits(new Vector(3, 3, 3), new Vector(5, 6, 6)));
        assertTrue(region.exits(new Vector(4, 4, 4), new Vector(4.1, 4.1, 4.1)));

        assertFalse(region.exits(new Vector(2, 2, 2), new Vector(3, 3, 3)));
        assertFalse(region.exits(new Vector(1, 1, 1), new Vector(4, 4, 4)));
        assertFalse(region.exits(new Vector(2, 3, 4), new Vector(4, 3, 2)));
    }
}
