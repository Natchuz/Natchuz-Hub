package com.natchuz.hub.paper.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.natchuz.hub.paper.Color.RESET;

/**
 * For easy ItemStack building
 */
public class StackBuilder {
    private final ItemStack stack;
    private final ItemMeta meta;

    public StackBuilder(Material material) {
        stack = new ItemStack(material);
        meta = stack.getItemMeta();
    }

    public StackBuilder(ItemStack model) {
        stack = new ItemStack(model);
        meta = stack.getItemMeta();
    }

    public StackBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public <T extends ItemMeta> StackBuilder meta(Class<T> type, Consumer<T> fun) {
        fun.accept((T) meta);
        return this;
    }

    public StackBuilder setUnbreakable() {
        meta.setUnbreakable(true);
        return this;
    }

    public StackBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    public StackBuilder enchantedGUI(boolean enchanted) {
        if (enchanted) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public StackBuilder enchant(int level, Enchantment... enchantments) {
        for (Enchantment e : enchantments) {
            meta.addEnchant(e, level, true);
        }
        return this;
    }

    public StackBuilder name(String name) {
        meta.setDisplayName(RESET + name);
        return this;
    }

    public StackBuilder lore(String lore) {
        if (lore == null)
            return this;
        if (meta.hasLore())
            meta.getLore().addAll(Arrays.asList(lore.split("\n")));
        else
            meta.setLore(Arrays.asList(lore.split("\n")));
        return this;
    }

    public StackBuilder hide(ItemFlag... flag) {
        meta.addItemFlags(flag);
        return this;
    }

    public ItemStack done() {
        stack.setItemMeta(meta);
        return stack;
    }

    public ItemStack doneGUI() {
        for (ItemFlag flag : ItemFlag.values()) {
            meta.addItemFlags(flag);
        }
        meta.setUnbreakable(true);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack appendLore(ItemStack stack, String lore) {
        ItemMeta meta = stack.getItemMeta();
        if (meta.getLore() != null) {
            List<String> currentLore = meta.getLore();
            currentLore.addAll(Arrays.asList(lore.split("\n")));
            meta.setLore(currentLore);
        } else {
            meta.setLore(Arrays.asList(lore.split("\n")));
        }
        stack.setItemMeta(meta);
        return stack;
    }
}
