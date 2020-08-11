package com.natchuz.paper.serialization;

import com.flowpowered.math.vector.Vector3d;
import org.junit.jupiter.api.Test;

import com.natchuz.hub.sponge.regions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RegionParserTest {

    @Test
    public void testForcedUnion() {
        assertEquals(new Union(new CuboidRegion(1, 1, 1, 2, 2, 2),
                        new CuboidRegion(-1, -1, -1, -2, -2, -2)),
                RegionParser.parse("cuboid(1 1 1 2 2 2); cuboid(-1 -1 -1 -2 -2 -2);", true));
        assertEquals(new Union(new CylinderRegion(new Vector3d(0.1, 0.5, -2), 4, 1),
                        new CuboidRegion(5, 2, 1, 6, 7, 4)),
                RegionParser.parse("cylinder(0.1 0.5 -2 4 1); cuboid(5 2 1 6 7 4);", true));
        assertEquals(new Union(new SphereRegion(new Vector3d(5, 5, -2), 4),
                        new CuboidRegion(3, 5, 1, 8, 9, 2)),
                RegionParser.parse("sphere(5 5 -2 4); cuboid(3 5 1 8 9 2);", true));

        assertEquals(new Union(), RegionParser.parse("", true));

        assertEquals(new Union(new CuboidRegion(1, 1, 1, 2, 2, 2)),
                RegionParser.parse("cuboid(1 1 1 2 2 2);", true));
    }

    @Test
    public void testRegularParser() {
        assertEquals(new Union(new CuboidRegion(1, 1, 1, 2, 2, 2),
                        new CuboidRegion(-1, -1, -1, -2, -2, -2)),
                RegionParser.parse("cuboid(1 1 1 2 2 2); cuboid(-1 -1 -1 -2 -2 -2);"));
        assertEquals(new Union(new CylinderRegion(new Vector3d(0.1, 0.5, -2), 4, 1),
                        new CuboidRegion(5, 2, 1, 6, 7, 4)),
                RegionParser.parse("cylinder(0.1 0.5 -2 4 1); cuboid(5 2 1 6 7 4);"));
        assertEquals(new Union(new SphereRegion(new Vector3d(5, 5, -2), 4),
                        new CuboidRegion(3, 5, 1, 8, 9, 2)),
                RegionParser.parse("sphere(5 5 -2 4); cuboid(3 5 1 8 9 2);"));

        assertEquals(new CuboidRegion(1.2, 3, 2, 1, 4, 4),
                RegionParser.parse("cuboid(1.2 3 2 1 4 4);"));
        assertEquals(new SphereRegion(new Vector3d(2.1, 4.1, -1), 4),
                RegionParser.parse("sphere(2.1 4.1 -1 4);"));

        assertNotEquals(new SphereRegion(new Vector3d(2.1, 4.1, -1), 4),
                RegionParser.parse("sphere(4.1 451 -4 1);"));
    }
}
