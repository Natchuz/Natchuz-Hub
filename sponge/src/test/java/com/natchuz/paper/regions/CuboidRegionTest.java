package com.natchuz.paper.regions;

import com.flowpowered.math.vector.Vector3d;
import org.junit.jupiter.api.Test;

import com.natchuz.hub.sponge.regions.CuboidRegion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CuboidRegionTest {

    @Test
    public void testContains() {
        Vector3d vectorMin = new Vector3d(1, 1, 1);
        Vector3d vectorMax = new Vector3d(4, 4, 4);

        CuboidRegion region = new CuboidRegion(vectorMin, vectorMax);

        assertTrue(region.contains(new Vector3d(2, 1, 3)));
        assertTrue(region.contains(new Vector3d(3, 3, 4)));
        assertTrue(region.contains(new Vector3d(3, 4, 1)));

        assertFalse(region.contains(new Vector3d(5, 4, 1)));
        assertFalse(region.contains(new Vector3d(3, 4.1, 1)));
        assertFalse(region.contains(new Vector3d(0, 4, 1)));
    }

    @Test
    public void testEnters() {
        Vector3d vectorMin = new Vector3d(1, 1, 1);
        Vector3d vectorMax = new Vector3d(4, 4, 4);

        CuboidRegion region = new CuboidRegion(vectorMin, vectorMax);

        assertTrue(region.enters(new Vector3d(0, 2, 2), new Vector3d(2, 2, 2)));
        assertTrue(region.enters(new Vector3d(4.1, 2, 2), new Vector3d(3, 2, 2)));
        assertTrue(region.enters(new Vector3d(5, 2, 2), new Vector3d(4, 2, 2)));

        assertFalse(region.enters(new Vector3d(-1, -1, -1), new Vector3d(-2, -2, -2)));
        assertFalse(region.enters(new Vector3d(0.1, 0.1, 0.1), new Vector3d(0, 1, 1.1)));
        assertFalse(region.enters(new Vector3d(3, 2, 1), new Vector3d(4, 1.1, 1)));
    }

    @Test
    public void testExit() {
        Vector3d vectorMin = new Vector3d(1, 1, 1);
        Vector3d vectorMax = new Vector3d(4, 4, 4);

        CuboidRegion region = new CuboidRegion(vectorMin, vectorMax);

        assertTrue(region.exits(new Vector3d(2, 2, 2), new Vector3d(0, 0, 0)));
        assertTrue(region.exits(new Vector3d(3, 3, 3), new Vector3d(5, 6, 6)));
        assertTrue(region.exits(new Vector3d(4, 4, 4), new Vector3d(4.1, 4.1, 4.1)));

        assertFalse(region.exits(new Vector3d(2, 2, 2), new Vector3d(3, 3, 3)));
        assertFalse(region.exits(new Vector3d(1, 1, 1), new Vector3d(4, 4, 4)));
        assertFalse(region.exits(new Vector3d(2, 3, 4), new Vector3d(4, 3, 2)));
    }
}
