package com.natchuz.hub.kitpvp.map;

import lombok.Value;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@Value
public class ArenaSpawn {
    Location location;
    ItemStack icon;
    String id;
}
