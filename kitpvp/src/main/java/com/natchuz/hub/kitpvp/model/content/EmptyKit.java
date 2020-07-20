package com.natchuz.hub.kitpvp.model.content;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.natchuz.hub.kitpvp.model.ItemSet;

public class EmptyKit {

    public static final Kit INSTANCE = new Kit("", new ItemStack(Material.BUCKET), ItemSet.builder().build());

}
