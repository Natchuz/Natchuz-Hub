package com.natchuz.hub.paper.managers;
/*
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.Stack;
*/
/**
 * Don't even ask me what is it
 */
public class DialogManager {/* implements Listener {

    static HashMap<Player, Stack<Dialog>> parenting = new HashMap<>();

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player) ||
                event.getClickedInventory().getType() != InventoryType.CHEST ||
                event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;


        Dialog dialog = parenting.get(event.getWhoClicked()).peek();
        if (!event.getClickedInventory().equals(dialog.inv)) return;

        event.setCancelled(true);

        int slot = event.getRawSlot();

        int invHeight = 6 - (dialog.height % 2);
        int verticalMargin = (invHeight - dialog.height) / 2;
        int horizontalMargin = (9 - dialog.width) / 2;

        int x = (slot % 9) - horizontalMargin;
        int y = (slot / 9) - verticalMargin;

        for (Dialog.DialogField field : dialog.fields) {
            if (field.x == x && field.y == y) {
                field.run.accept(event.getClick());
                if (!dialog.ignored)
                    dialog.construct();
                dialog.ignored = false;
                return;
            }
        }
    }

    @EventHandler
    private void onInventoryCloseEvent(InventoryCloseEvent event) {
        Stack<Dialog> stack = parenting.get(event.getPlayer());

        if (stack == null || stack.isEmpty()) return;

        Dialog dialog = stack.peek();

        if (!dialog.ignored && event.getInventory().equals(dialog.inv)) {
            parenting.get(event.getPlayer()).forEach(Dialog::closed);
            parenting.get(event.getPlayer()).clear();
        }
    }

    public static void openDialog(Dialog dialog, Player player) {
        parenting.computeIfAbsent(player, k -> new Stack<>());

        dialog.viewer = player;
        dialog.init();
        parenting.get(player).push(dialog).construct();
    }*/
}
