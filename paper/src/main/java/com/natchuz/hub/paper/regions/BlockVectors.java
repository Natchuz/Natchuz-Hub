package com.natchuz.hub.paper.regions;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.apache.commons.lang.Validate;
import org.spongepowered.api.world.Location;

public class BlockVectors {

    public static Vector3d center(Vector3i blockPos) {
        Validate.notNull(blockPos);
        return new Vector3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

    public static Vector3d center(Vector3d pos) {
        Validate.notNull(pos);
        return center(pos.toInt());
    }

    public static Location<?> center(Location<?> location) {
        Validate.notNull(location);
        return location.copy().setPosition(center(location.getPosition()));
    }

    /**
     * Center location only in x and z axis
     */
    public static Location<?> centerFlat(Location<?> location) {
        Validate.notNull(location);
        Vector3i blockPosition = location.getBlockPosition();
        Vector3d position = location.getPosition();
        return location.copy().setPosition(
                new Vector3d(blockPosition.getX() + 0.5, position.getY(), blockPosition.getZ() + 0.5));
    }

    public static boolean isInside(Vector3d point, Location<?> blockLocation) {
        Validate.notNull(point);
        Validate.notNull(blockLocation);
        return blockLocation.getX() <= point.getX()
                && point.getX() <= blockLocation.getX() + 1
                && blockLocation.getY() <= point.getY()
                && point.getY() <= blockLocation.getY() + 1
                && blockLocation.getZ() <= point.getZ()
                && point.getZ() <= blockLocation.getZ() + 1;
    }
}
