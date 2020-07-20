package com.natchuz.hub.kitpvp.impl.ui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

import com.natchuz.hub.paper.items.StackBuilder;
import com.natchuz.hub.paper.managers.Dialog;
import com.natchuz.hub.core.content.Items;
import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.kitpvp.model.KitProfile;
import com.natchuz.hub.kitpvp.model.content.ItemShop;
import com.natchuz.hub.kitpvp.model.content.ItemShopEntry;

public class ItemShopDialog extends Dialog {

    private final ProfileRepo<KitProfile> profileProvider;
    private KitProfile user;
    private final ItemShop shop;
    private final BiConsumer<ItemStack, Player> transactionHandler;
    private final int page = 0;

    public ItemShopDialog(ItemShop shop, ProfileRepo<KitProfile> profileProvider,
                          BiConsumer<ItemStack, Player> transactionHandler) {
        this.shop = shop;
        this.profileProvider = profileProvider;
        this.transactionHandler = transactionHandler;
    }

    @Override
    protected void init() {
        setHeight(5);
        setWidth(7);
        setName("Item shop");

        user = profileProvider.getProfile(viewer());
    }

    @Override
    protected void build() {
        setHeight(6);
        setWidth(7);

        for (int i = page * 4 * 7; i < (page + 1) * 4 * 7; i++) {
            int x = i % 7;
            int y = (i / 7) + 1;

            if (i >= shop.getItems().size()) {
                setField(new ItemStack(Material.AIR), x, y, (c) -> {
                });
                continue;
            }

            ItemShopEntry entry = shop.getItems().get(i);

            setField(new StackBuilder(entry.getStack()).lore("Cost: " + entry.getCost()).doneGUI(), x, y, (c) -> {
                if (user.getCoins() >= entry.getCost()) {
                    user.setCoins(user.getCoins() - entry.getCost());
                    profileProvider.updateProfile(user);
                    transactionHandler.accept(entry.getStack(), viewer());
                    viewer().playSound(viewer().getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
                } else {
                    viewer().playSound(viewer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                }
            });
        }

        setField(Items.Gui.BACK, 3, 5, (c) -> close());
    }

}
