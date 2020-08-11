package com.natchuz.hub.kitpvp.impl.ui;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

import com.natchuz.hub.sponge.managers.Dialog;
import com.natchuz.hub.core.content.Items;
import com.natchuz.hub.kitpvp.ConcreteKitGamemode;
import com.natchuz.hub.kitpvp.model.content.Kit;

import static com.natchuz.hub.sponge.Color.*;

@AllArgsConstructor
public class KitSelector extends Dialog {

    private final ConcreteKitGamemode gamemode;
    private final Consumer<Kit> kitCallback;

    @Override
    protected void init() {
        setName("Select kit");
        setWidth(7);
        setHeight(4);
    }

    @Override
    protected void build() {
        List<Kit> kits = gamemode.getKitConfig().getDefaultKits();

        for (int i = 0; i < 7 * 5; i++) {
            ItemStack target;
            Consumer<ClickType> action = c -> {
            };

            if (kits.size() > i) {
                Kit kit = kits.get(i);
                target = new StackBuilder(kit.getIcon().clone())
                        .name(GREEN + BOLD + "Select: " + AQUA + BOLD + kit.getName())
                        .lore("\n" + GREY + "Click to select")
                        .doneGUI();
                action = c -> {
                    kitCallback.accept(kit);
                    close();
                };
            } else {
                target = new ItemStack(Material.AIR);
            }

            int x = i % 7;
            int y = i / 7;
            setField(target, x, y, action);
        }

        setField(Items.Gui.BACK, 3, 4, c -> close());
    }
}
