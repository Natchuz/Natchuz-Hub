package com.natchuz.hub.paper.items;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    /**
     * Compares ItemStacks by name
     *
     * @param i1 first ItemStack
     * @param i2 second ItemStack
     * @return if compared ItemStacks have the same name
     */
    public static boolean equalsName(ItemStack i1, ItemStack i2) {
        return i1.hasItemMeta() == i2.hasItemMeta() && i1.getItemMeta().getDisplayName().equals(i2.getItemMeta().getDisplayName());
    }

    /**
     * Compares ItemStacks by ItemMeta.
     *
     * @param i1 first ItemStack
     * @param i2 second ItemStack
     * @return if compared ItemStacks have the same meta
     */
    public static boolean similar(ItemStack i1, ItemStack i2) {
        return i1.hasItemMeta() == i2.hasItemMeta() && (!i1.hasItemMeta() || Bukkit.getItemFactory().equals(i1.getItemMeta(), i2.getItemMeta()));
    }
}
