package com.natchuz.hub.core.content.ui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.natchuz.hub.paper.items.StackBuilder;
import com.natchuz.hub.paper.managers.Dialog;

import static com.natchuz.hub.paper.Color.*;

/**
 * A dialog that ask for confirmation
 */
public class ConfirmationDialog extends Dialog {

    private final Runnable confirm;
    private final Runnable decline;
    private final String title;
    private String confirmText;
    private String declineText;
    private final ItemStack icon;

    /**
     * @param confirm action performed when player answers positive
     * @param decline action performed when player answers negative
     * @param icon    icon to be displayed
     * @param title   title of dialog
     */
    public ConfirmationDialog(Runnable confirm, Runnable decline, ItemStack icon, String title) {
        this.confirm = confirm;
        this.decline = decline;
        this.icon = icon;
        this.title = title;

        setHeight(3);
        setWidth(3);
        setName(title);
    }

    /**
     * @param confirm action performed when player answers positive
     * @param decline action performed when player answers negative
     * @param icon    icon to be displayed
     */
    public ConfirmationDialog(Runnable confirm, Runnable decline, ItemStack icon) {
        this(confirm, decline, icon, "Confirm?");
    }

    @Override
    protected void build() {
        setField(icon, 1, 0, (c) -> {
        });
        setField(new StackBuilder(Material.RED_WOOL).name(RED + BOLD + "Cancel").doneGUI(), 0, 2, (c) -> {
            decline.run();
            close();
        });
        setField(new StackBuilder(Material.GREEN_WOOL).name(GREEN + BOLD + "Confirm").doneGUI(), 2, 2, (c) -> {
            confirm.run();
            close();
        });
    }
}
