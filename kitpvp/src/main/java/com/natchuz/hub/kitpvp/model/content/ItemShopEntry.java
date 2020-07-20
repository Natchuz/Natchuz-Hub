package com.natchuz.hub.kitpvp.model.content;

import org.bukkit.inventory.ItemStack;

public class ItemShopEntry {

    private final ItemStack stack;
    private final int cost;

    public ItemShopEntry(ItemStack stack, int cost) {
        this.stack = stack;
        this.cost = cost;
    }

    public ItemStack getStack() {
        return stack.clone();
    }

    public int getCost() {
        return cost;
    }
}
