package com.natchuz.hub.core.content;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

import com.natchuz.hub.paper.items.StackBuilder;

import static com.natchuz.hub.paper.Color.*;
import static org.bukkit.Material.*;

/**
 * This class contains all default items / icons used in every server
 */
public class Items {

    /**
     * All gui items
     */
    public static class Gui {
        public static final ItemStack BACK = new StackBuilder(ARROW).name(YELLOW + "Back").doneGUI();

        public static final ItemStack NEXT = new StackBuilder(PLAYER_HEAD).name("Next")
                .meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("9e3a267d-31cc-4020-bfad-a553907a853b")))).doneGUI();

        public static final ItemStack NEXT_DISABLED = new StackBuilder(PLAYER_HEAD).name("Next")
                .meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("8d48c819-2b2c-4f7c-9c93-05e769cbcd6c")))).doneGUI();

        public static final ItemStack PREVIOUS = new StackBuilder(PLAYER_HEAD).name("Previous")
                .meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("ffe36429-5ec9-47ac-b48c-8ca547159d02")))).doneGUI();

        public static final ItemStack PREVIOUS_DISABLED = new StackBuilder(PLAYER_HEAD).name("Previous")
                .meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("b5f06fdc-968f-43e7-9a3b-367659a56aff")))).doneGUI();
    }

    public static final ItemStack COSMETICS = new StackBuilder(ENDER_CHEST).name("Cosmetics").doneGUI();

    public static final ItemStack SERVER_SWITCH = new StackBuilder(Material.CLOCK).name(GOLD + BOLD + "Switch server").doneGUI();

    public static final ItemStack FRIENDS = new StackBuilder(PLAYER_HEAD).name(AQUA + BOLD + "Friends")
            .meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("d7670d8c-148d-4d54-87c1-8c168d760638")))).doneGUI();

}