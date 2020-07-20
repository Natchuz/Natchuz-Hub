package com.natchuz.hub.kitpvp.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.natchuz.hub.kitpvp.model.ItemSet;

public class InventoryUtils {

    public static void applySet(ItemSet set, Player player) {
        PlayerInventory inventory = player.getInventory();

        set.getHelmet().ifPresent(inventory::setHelmet);
        set.getChestplate().ifPresent(inventory::setChestplate);
        set.getLeggings().ifPresent(inventory::setLeggings);
        set.getBoots().ifPresent(inventory::setBoots);
        set.getHelmet().ifPresent(inventory::setHelmet);

        inventory.addItem(set.getBar().toArray(new ItemStack[0]));
    }

}
