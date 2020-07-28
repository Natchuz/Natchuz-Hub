package com.natchuz.paper.regions;

import com.flowpowered.math.vector.Vector3d;
import org.junit.jupiter.api.Test;

import com.natchuz.hub.paper.regions.SphereRegion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SphereRegionTest {

    @Test
    public void testContains() {
        SphereRegion region = new SphereRegion(new Vector3d(5, 5, 5), 5);

        assertTrue(region.contains(new Vector3d(5, 5, 0)));
        assertTrue(region.contains(new Vector3d(5, 0, 5)));
        assertTrue(region.contains(new Vector3d(3, 4, 1)));

        assertFalse(region.contains(new Vector3d(0, 10, 10)));
        assertFalse(region.contains(new Vector3d(-1, 4.1, -5)));
        assertFalse(region.contains(new Vector3d(0, 4, 1)));
    }

    @Test
    public void testEnters() {
        SphereRegion region = new SphereRegion(new Vector3d(0, 0, 0), 5);

        assertTrue(region.enters(new Vector3d(0, -6, 0), new Vector3d(0, 5, 0)));
        assertTrue(region.enters(new Vector3d(6, 0, 0), new Vector3d(3, 2, 2)));
        assertTrue(region.enters(new Vector3d(5, 5, 5), new Vector3d(3, 2, Math.sqrt(12))));

        assertFalse(region.enters(new Vector3d(-1, -1, -1), new Vector3d(-2, -2, -2)));
        assertFalse(region.enters(new Vector3d(0.1, 0.1, 0.1), new Vector3d(0, 1, 1.1)));
        assertFalse(region.enters(new Vector3d(3, 2, 1), new Vector3d(4, 1.1, 1)));
    }

    @Test
    public void testExit() {
        SphereRegion region = new SphereRegion(new Vector3d(5, 5, 5), 5);

        assertTrue(region.exits(new Vector3d(3, 2, Math.sqrt(12)), new Vector3d(10, 10, 10)));
        assertTrue(region.exits(new Vector3d(3, 2, 2), new Vector3d(6, -1, -1)));
        assertTrue(region.exits(new Vector3d(3, 2, Math.sqrt(12)), new Vector3d(-10, -10, -10)));

        assertFalse(region.exits(new Vector3d(2, 2, 2), new Vector3d(3, 3, 3)));
        assertFalse(region.exits(new Vector3d(1, 1, 1), new Vector3d(4, 4, 4)));
        assertFalse(region.exits(new Vector3d(2, 3, 4), new Vector3d(4, 3, 2)));
    }
}
