package com.natchuz.hub.paper.regions;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class BlockVectors {

    public static BlockVector center(Vector blockPos) {
        Validate.notNull(blockPos);
        return new BlockVector(
                blockPos.getBlockX() + 0.5, blockPos.getBlockY() + 0.5, blockPos.getBlockZ() + 0.5);
    }

    public static Location center(Location location) {
        Validate.notNull(location);
        Location center = location.clone();
        center.setX(center.getBlockX() + 0.5);
        center.setY(center.getBlockY() + 0.5);
        center.setZ(center.getBlockZ() + 0.5);
        return center;
    }

    public static Location center(Block block) {
        Validate.notNull(block);
        return center(block.getLocation());
    }

    /**
     * Center location only in x and z axis
     */
    public static Location centerFlat(Location location) {
        Validate.notNull(location);
        Location center = location.clone();
        center.setX(center.getBlockX() + 0.5);
        center.setY(center.getY());
        center.setZ(center.getBlockZ() + 0.5);
        return center;
    }

    public static boolean isInside(Vector point, Location blockLocation) {
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
