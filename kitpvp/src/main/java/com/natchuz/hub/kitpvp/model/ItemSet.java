package com.natchuz.hub.kitpvp.model;

import lombok.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * Item set represents yes
 */
@ToString(doNotUseGetters = true)
@EqualsAndHashCode
@Builder
@Setter
@AllArgsConstructor
public class ItemSet {
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack offHand;

    @Getter
    @Singular("bar")
    private List<ItemStack> bar;

    @Getter
    @Singular("hidden")
    private List<ItemStack> hidden;

    private ItemSet() {
    }

    public Optional<ItemStack> getHelmet() {
        return Optional.ofNullable(helmet);
    }

    public Optional<ItemStack> getChestplate() {
        return Optional.ofNullable(chestplate);
    }

    public Optional<ItemStack> getLeggings() {
        return Optional.ofNullable(leggings);
    }

    public Optional<ItemStack> getBoots() {
        return Optional.ofNullable(boots);
    }

    public Optional<ItemStack> getOffHand() {
        return Optional.ofNullable(offHand);
    }
}
