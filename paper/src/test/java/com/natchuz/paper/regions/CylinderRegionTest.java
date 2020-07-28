package com.natchuz.paper.regions;

import com.flowpowered.math.vector.Vector3d;
import org.junit.jupiter.api.Test;

import com.natchuz.hub.paper.regions.CylinderRegion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CylinderRegionTest {

    @Test
    public void testContains() {
        CylinderRegion region = new CylinderRegion(new Vector3d(0, 0, 0), 5, 5);

        assertTrue(region.contains(new Vector3d(5, 5, 0)));
        assertTrue(region.contains(new Vector3d(3, 0, 4)));
        assertTrue(region.contains(new Vector3d(2, 2, 2)));

        assertFalse(region.contains(new Vector3d(0, 10, 10)));
        assertFalse(region.contains(new Vector3d(-1, 4.1, -5)));
        assertFalse(region.contains(new Vector3d(0, 4, 10)));
    }

    @Test
    public void testEnters() {
        CylinderRegion region = new CylinderRegion(new Vector3d(0, 0, 0), 5, 5);

        assertTrue(region.enters(new Vector3d(0, -6, 0), new Vector3d(0, 5, 0)));
        assertTrue(region.enters(new Vector3d(6, 0, 0), new Vector3d(3, 2, 2)));
        assertTrue(region.enters(new Vector3d(5, 5, 5), new Vector3d(3, 2, Math.sqrt(12))));

        assertFalse(region.enters(new Vector3d(-1, -1, -1), new Vector3d(-2, -2, -2)));
        assertFalse(region.enters(new Vector3d(0.1, 0.1, 0.1), new Vector3d(0, 1, 1.1)));
        assertFalse(region.enters(new Vector3d(3, 2, 1), new Vector3d(4, 1.1, 1)));
    }

    @Test
    public void testExit() {
        CylinderRegion region = new CylinderRegion(new Vector3d(0, 0, 0), 5, 5);

        assertTrue(region.exits(new Vector3d(3, 2, Math.sqrt(12)), new Vector3d(10, 10, 10)));
        assertTrue(region.exits(new Vector3d(3, 2, 2), new Vector3d(6, -1, -1)));
        assertTrue(region.exits(new Vector3d(3, 2, Math.sqrt(12)), new Vector3d(-10, -10, -10)));

        assertFalse(region.exits(new Vector3d(2, 2, 2), new Vector3d(3, 3, 3)));
        assertFalse(region.exits(new Vector3d(1, 1, 1), new Vector3d(0, 2, 0)));
        assertFalse(region.exits(new Vector3d(0, 5, 0), new Vector3d(1, 0.1, 1)));
    }
}
