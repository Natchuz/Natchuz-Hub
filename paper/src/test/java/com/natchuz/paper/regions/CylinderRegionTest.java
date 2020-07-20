package com.natchuz.paper.regions;

import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;

import com.natchuz.hub.paper.regions.CylinderRegion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CylinderRegionTest {

    @Test
    public void testContains() {
        CylinderRegion region = new CylinderRegion(new Vector(0, 0, 0), 5, 5);

        assertTrue(region.contains(new Vector(5, 5, 0)));
        assertTrue(region.contains(new Vector(3, 0, 4)));
        assertTrue(region.contains(new Vector(2, 2, 2)));

        assertFalse(region.contains(new Vector(0, 10, 10)));
        assertFalse(region.contains(new Vector(-1, 4.1, -5)));
        assertFalse(region.contains(new Vector(0, 4, 10)));
    }

    @Test
    public void testEnters() {
        CylinderRegion region = new CylinderRegion(new Vector(0, 0, 0), 5, 5);

        assertTrue(region.enters(new Vector(0, -6, 0), new Vector(0, 5, 0)));
        assertTrue(region.enters(new Vector(6, 0, 0), new Vector(3, 2, 2)));
        assertTrue(region.enters(new Vector(5, 5, 5), new Vector(3, 2, Math.sqrt(12))));

        assertFalse(region.enters(new Vector(-1, -1, -1), new Vector(-2, -2, -2)));
        assertFalse(region.enters(new Vector(0.1, 0.1, 0.1), new Vector(0, 1, 1.1)));
        assertFalse(region.enters(new Vector(3, 2, 1), new Vector(4, 1.1, 1)));
    }

    @Test
    public void testExit() {
        CylinderRegion region = new CylinderRegion(new Vector(0, 0, 0), 5, 5);

        assertTrue(region.exits(new Vector(3, 2, Math.sqrt(12)), new Vector(10, 10, 10)));
        assertTrue(region.exits(new Vector(3, 2, 2), new Vector(6, -1, -1)));
        assertTrue(region.exits(new Vector(3, 2, Math.sqrt(12)), new Vector(-10, -10, -10)));

        assertFalse(region.exits(new Vector(2, 2, 2), new Vector(3, 3, 3)));
        assertFalse(region.exits(new Vector(1, 1, 1), new Vector(0, 2, 0)));
        assertFalse(region.exits(new Vector(0, 5, 0), new Vector(1, 0.1, 1)));
    }
}
