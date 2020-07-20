package com.natchuz.paper.regions;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.natchuz.hub.paper.regions.BlockVectors;

public class BlockVectorsTest {

    @Test
    public void testCenterLocation() {
        Location location = new Location(null, 10, 2, 3);

        location = BlockVectors.center(location);

        Assertions.assertEquals(10.5, location.getX());
        Assertions.assertEquals(2.5, location.getY());
        Assertions.assertEquals(3.5, location.getZ());
    }

    @Test
    public void testCenterFlat() {
        Location location = new Location(null, 10.1, 2.1, 3.3);

        location = BlockVectors.centerFlat(location);

        Assertions.assertEquals(10.5, location.getX());
        Assertions.assertEquals(2.1, location.getY());
        Assertions.assertEquals(3.5, location.getZ());
    }

    @Test
    public void testCenterVector() {
        Vector vector = new Vector(10.1, 2.1, 3.3);

        vector = BlockVectors.center(vector);

        Assertions.assertEquals(10.5, vector.getX());
        Assertions.assertEquals(2.5, vector.getY());
        Assertions.assertEquals(3.5, vector.getZ());
    }

    @Test
    public void testIsInside() {
        Location location = new Location(null, 3, 1, 4);

        Assertions.assertTrue(BlockVectors.isInside(new Vector(3, 1, 4), location));
        Assertions.assertTrue(BlockVectors.isInside(new Vector(3.5, 1, 4), location));
        Assertions.assertTrue(BlockVectors.isInside(new Vector(3.1, 1.2, 4.5), location));
        Assertions.assertTrue(BlockVectors.isInside(new Vector(3.7, 1.9, 4.9), location));

        Assertions.assertFalse(BlockVectors.isInside(new Vector(4.1, 1, 4), location));
        Assertions.assertFalse(BlockVectors.isInside(new Vector(1, 5, 6), location));
        Assertions.assertFalse(BlockVectors.isInside(new Vector(3, 1.4, 5.1), location));
        Assertions.assertFalse(BlockVectors.isInside(new Vector(-1, 0, 4), location));
    }

}
