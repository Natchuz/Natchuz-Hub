package com.natchuz.hub.core.content;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * This class contains all default items / icons used in every server
 */
public class Items {

    private static ItemStack.Builder guiBuilder() {
        return ItemStack.builder()
                .add(Keys.HIDE_ATTRIBUTES, true)
                .add(Keys.HIDE_CAN_DESTROY, true)
                .add(Keys.HIDE_CAN_PLACE, true)
                .add(Keys.HIDE_ENCHANTMENTS, true)
                .add(Keys.HIDE_MISCELLANEOUS, true)
                .add(Keys.HIDE_UNBREAKABLE, true);
    }

    /**
     * All gui items
     */
    public static class Gui {

        public static final ItemStack BACK = guiBuilder()
                .itemType(ItemTypes.ARROW)
                .add(Keys.DISPLAY_NAME, Text.of("Back", TextColors.YELLOW))
                .build();

        public static final ItemStack NEXT = /*guiBuilder().itemType(ItemTypes.SKULL)
                    .add(Keys.SKULL_TYPE, SkullTypes.PLAYER)
                    .apply(() -> true, b -> {
                        b.add(Keys.REPRESENTED_PLAYER, Sponge.getServer().getGameProfileManager().get(UUID.fromString("9e3a267d-31cc-4020-bfad-a553907a853b")).get())
                    })
                    .build();*/
                guiBuilder().itemType(ItemTypes.ARROW).build();

        public static final ItemStack NEXT_DISABLED = /*new StackBuilder(PLAYER_HEAD).name("Next")
                .meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("8d48c819-2b2c-4f7c-9c93-05e769cbcd6c")))).doneGUI();*/
                guiBuilder().itemType(ItemTypes.ARROW).build();

        public static final ItemStack PREVIOUS = /*new StackBuilder(PLAYER_HEAD).name("Previous")
                .meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("ffe36429-5ec9-47ac-b48c-8ca547159d02")))).doneGUI();*/
                guiBuilder().itemType(ItemTypes.ARROW).build();

        public static final ItemStack PREVIOUS_DISABLED = /*new StackBuilder(PLAYER_HEAD).name("Previous")
                .meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("b5f06fdc-968f-43e7-9a3b-367659a56aff")))).doneGUI();*/
                guiBuilder().itemType(ItemTypes.ARROW).build();
    }

    public static final ItemStack COSMETICS = /*new StackBuilder(ENDER_CHEST).name("Cosmetics").doneGUI();*/
            guiBuilder().itemType(ItemTypes.ARROW).build();

    public static final ItemStack SERVER_SWITCH = //new StackBuilder(Material.CLOCK).name(GOLD + BOLD + "Switch server").doneGUI();
            guiBuilder().itemType(ItemTypes.ARROW).build();

    public static final ItemStack FRIENDS = /*new StackBuilder(PLAYER_HEAD).name(AQUA + BOLD + "Friends")
            .meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("d7670d8c-148d-4d54-87c1-8c168d760638")))).doneGUI();*/
            guiBuilder().itemType(ItemTypes.ARROW).build();

}