package com.natchuz.hub.paper.managers;

/*import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.function.Consumer;*/

/**
 * Spaghetti here
 */
public abstract class Dialog {
/*
    Inventory inv;
    LinkedList<DialogField> fields = new LinkedList<>();
    Player viewer;

    boolean ignored = false;

    String name = "";
    int width = 1;
    int height = 1;

    void construct() {
        fields.clear();
        build();

        int invHeight = 6 - (height % 2);
        inv = Bukkit.createInventory(viewer, invHeight * 9, name);

        int verticalMargin = (invHeight - height) / 2;
        int horizontalMargin = (9 - width) / 2;

        buildLoop:
        for (int i = 0; i < invHeight * 9; i++) {
            int x = i % 9;
            int y = i / 9;

            for (DialogField field : fields) {
                if (field.x == x - horizontalMargin && field.y == y - verticalMargin) {
                    inv.setItem(i, field.itemStack);
                    continue buildLoop;
                }
            }
            ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);
            inv.setItem(i, glass);
        }

        ignored = true;
        viewer.openInventory(inv);
        ignored = false;
    }

    //region construction methods

    static class DialogField {
        int x;
        int y;
        ItemStack itemStack;
        Consumer<ClickType> run;
    }

    protected final void setField(ItemStack stack, int x, int y, Consumer<ClickType> run) {
        Dialog.DialogField field = new Dialog.DialogField();
        field.x = x;
        field.y = y;
        field.itemStack = stack;
        field.run = run;
        fields.add(field);
    }

    protected final void setField(ItemStack stack, int x, int y, Runnable left, Runnable right) {
        setField(stack, x, y, (c) -> {
            if (c == ClickType.LEFT)
                left.run();
            else
                right.run();
        });
    }

    protected final void setName(String name) {
        this.name = name;
    }

    protected final void setWidth(int width) {
        this.width = width;
    }

    protected final void setHeight(int height) {
        this.height = height;
    }

    protected final Player viewer() {
        return viewer;
    }

    //endregion
    //region navigating methods

    protected final void open(Dialog dialog) {
        ignored = true;
        DialogManager.openDialog(dialog, viewer);
    }

    protected final void navigate(Dialog dialog) {
        ignored = false;
        DialogManager.parenting.get(viewer).pop().closed();
        open(dialog);
    }

    protected final void close() {
        ignored = true;
        if (DialogManager.parenting.get(viewer).size() == 1) {
            closed();
            DialogManager.parenting.get(viewer).pop();
            viewer.closeInventory();
        } else {
            closed();
            DialogManager.parenting.get(viewer).pop();
            DialogManager.parenting.get(viewer).peek().construct();
        }
    }

    protected abstract void build();

    protected void closed() {
    }

    protected void init() {
    }

    //endregion*/
}
