package com.natchuz.hub.core.content.ui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import com.natchuz.hub.paper.managers.Dialog;
import com.natchuz.hub.core.content.Items;
import com.natchuz.hub.core.content.cosmetics.Cosmetics;

import static com.natchuz.hub.paper.Color.*;

/**
 * Dialog that manages cosmetics
 */
public class CosmeticDialog extends Dialog {
    private Class<? extends Enum> context;
    private final int page = 0;

    @Override
    protected void init() {
        setName("Cosmetics");
    }

    @Override
    protected void build() {
        if (context == null) {
            setWidth(7);
            setHeight(1);
            setField(new StackBuilder(Material.TIPPED_ARROW).name(RED + BOLD + "Arrow traits").doneGUI(), 0, 0, (c) -> context = Cosmetics.Traits.class);
            setField(new StackBuilder(Material.SHIELD).name(YELLOW + BOLD + "Shields").doneGUI(), 3, 0, (c) -> context = Cosmetics.Shields.class);
            setField(new StackBuilder(Material.RED_DYE).name(DARK_RED + BOLD + "Blood effects").doneGUI(), 6, 0, (c) -> context = Cosmetics.BloodEffects.class);
        } else {
            setHeight(6);
            setWidth(7);

            for (int i = page * 4 * 7; i < (page + 1) * 4 * 7; i++) {
                int x = i % 7;
                int y = (i / 7) + 1;

                if (i >= context.getEnumConstants().length) {
                    setField(new ItemStack(Material.AIR), x, y, (c) -> {
                    });
                    continue;
                }

                Cosmetics.Cosmetic cosm = (Cosmetics.Cosmetic) context.getEnumConstants()[i];

                setField(new StackBuilder(cosm.icon(viewer())).name(cosm.title()).lore(cosm.description(viewer())).doneGUI(), x, y, (c) -> {
                    if (cosm.select(viewer())) {
                        viewer().playSound(viewer().getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
                    } else {
                        viewer().playSound(viewer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    }
                });
            }

            setField(Items.Gui.BACK, 3, 5, (c) -> context = null);
        }
    }
}
