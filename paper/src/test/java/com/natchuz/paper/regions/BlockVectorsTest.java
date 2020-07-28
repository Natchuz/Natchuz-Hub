package com.natchuz.paper.regions;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.natchuz.hub.paper.regions.BlockVectors;

import static org.mockito.Mockito.mock;

public class BlockVectorsTest {

    @Test
    public void testCenterLocation() {
        Location<?> location = new Location<>(mock(World.class), 10, 2, 3);

        location = BlockVectors.center(location);

        Assertions.assertEquals(10.5, location.getX());
        Assertions.assertEquals(2.5, location.getY());
        Assertions.assertEquals(3.5, location.getZ());
    }

    @Test
    public void testCenterVector3d() {
        Vector3d vector = new Vector3d(10.1, 2.1, 3.3);

        vector = BlockVectors.center(vector);

        Assertions.assertEquals(10.5, vector.getX());
        Assertions.assertEquals(2.5, vector.getY());
        Assertions.assertEquals(3.5, vector.getZ());
    }

    @Test
    public void testCenterVector3i() {
        Vector3i vector = new Vector3i(10.1, 2.1, 3.3);
        Vector3d out = BlockVectors.center(vector);

        Assertions.assertEquals(10.5, out.getX());
        Assertions.assertEquals(2.5, out.getY());
        Assertions.assertEquals(3.5, out.getZ());
    }

    @Test
    public void testCenterFlat() {
        Location<?> location = new Location<>(mock(World.class), 10.1, 2.1, 3.3);
        location = BlockVectors.centerFlat(location);

        Assertions.assertEquals(10.5, location.getX());
        Assertions.assertEquals(2.1, location.getY());
        Assertions.assertEquals(3.5, location.getZ());
    }

    @Test
    public void testIsInside() {
        Location<?> location = new Location<>(mock(World.class), 3, 1, 4);

        Assertions.assertTrue(BlockVectors.isInside(new Vector3d(3, 1, 4), location));
        Assertions.assertTrue(BlockVectors.isInside(new Vector3d(3.5, 1, 4), location));
        Assertions.assertTrue(BlockVectors.isInside(new Vector3d(3.1, 1.2, 4.5), location));
        Assertions.assertTrue(BlockVectors.isInside(new Vector3d(3.7, 1.9, 4.9), location));

        Assertions.assertFalse(BlockVectors.isInside(new Vector3d(4.1, 1, 4), location));
        Assertions.assertFalse(BlockVectors.isInside(new Vector3d(1, 5, 6), location));
        Assertions.assertFalse(BlockVectors.isInside(new Vector3d(3, 1.4, 5.1), location));
        Assertions.assertFalse(BlockVectors.isInside(new Vector3d(-1, 0, 4), location));
    }

}
