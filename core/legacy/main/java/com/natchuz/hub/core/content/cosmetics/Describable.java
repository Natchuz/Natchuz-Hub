package com.natchuz.hub.core.content.cosmetics;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

public interface Describable {
    ItemStack icon();

    String title();

    String description();

    default ItemStack icon(Player player) {
        return icon();
    }

    default String description(Player player) {
        return description();
    }
}
