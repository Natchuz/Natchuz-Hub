package com.natchuz.hub.core.content.cosmetics;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.Arrays;
import java.util.function.Consumer;

import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.user.Purchasable;

import static com.natchuz.hub.paper.Color.*;

/**
 * This class contains all cosmetics
 */
public class Cosmetics {

    /**
     * Arrow traits
     */
    public enum Traits implements Cosmetic {
        NONE(null, 0, Material.BLACK_STAINED_GLASS, "None", "OG particle"),
        NOTE(Particle.NOTE, 80, Material.NOTE_BLOCK, "Note", "So, you're a musician?"),
        FLAME(Particle.FLAME, 81, Material.BLAZE_POWDER, "Fire", "Hellfire, Hellfire, take my soul!"),
        SNOW(Particle.SNOWBALL, 82, Material.SNOWBALL, "Snow", "It's cold outside!");

        private final Particle particle;
        private final int id;
        private final Material icon;
        private final String name;
        private final String desc;

        Traits(Particle particle, int id, Material icon, String name, String desc) {
            this.particle = particle;
            this.id = id;
            this.icon = icon;
            this.name = name;
            this.desc = desc;
        }

        @Override
        public Integer getItemId() {
            return id;
        }

        public Particle getParticle() {
            return particle;
        }

        public boolean selected(Player player) {
            return NetworkMain.getInstance().getProfile(player).getSelectedTrait() == this;
        }

        @Override
        public boolean select(Player player) {
            if (ownedBy(player)) {
                NetworkMain.getInstance().getProfile(player).setSelectedTrait(this);
            }
            return ownedBy(player);
        }

        @Override
        public ItemStack icon() {
            return new ItemStack(icon);
        }

        @Override
        public String title() {
            return RED + BOLD + name;
        }

        @Override
        public String description() {
            return GREY + desc + RESET;
        }
    }

    /**
     * Shields patterns
     */
    public enum Shields implements Cosmetic {
        NONE(new Pattern[]{}, 0, "Default", "Newly forged wooden shield"),
        CRUSADER(new Pattern[]{new Pattern(DyeColor.RED, PatternType.STRAIGHT_CROSS)}, 0, "Lorem ipsum", "Let's take the Jerusalem");

        private final Pattern[] pattern;
        private final int id;
        private final String name;
        private final String quote;

        Shields(Pattern[] pattern, int id, String name, String quote) {
            this.pattern = pattern;
            this.id = id;
            this.name = name;
            this.quote = quote;
        }

        public Pattern[] getPattern() {
            return pattern;
        }

        @Override
        public Integer getItemId() {
            return id;
        }

        @Override
        public boolean select(Player player) {
            if (ownedBy(player)) {
                NetworkMain.getInstance().getProfile(player).setSelectedShield(this);
            }
            return ownedBy(player);
        }

        @Override
        public ItemStack icon() {
            return new StackBuilder(Material.SHIELD).meta(BlockStateMeta.class, (m) -> {
                BlockState state = m.getBlockState();
                Banner bannerState = (Banner) state;
                bannerState.setPatterns(Arrays.asList(getPattern()));
                m.setBlockState(state);
            }).doneGUI();
        }

        @Override
        public String title() {
            return YELLOW + BOLD + name;
        }

        @Override
        public String description() {
            return ITALIC + YELLOW + quote;
        }
    }

    /**
     * Blood effects
     */
    public enum BloodEffects implements Cosmetic {
        NORMAL((p) -> {
            BlockData data = Material.REDSTONE_BLOCK.createBlockData();
            for (int i = 0; i < 10; i++) {
                p.getLocation().getWorld().spawnParticle(Particle.BLOCK_CRACK, p.getLocation().add(
                        Math.random() - 0.5,
                        Math.random() * 1.5,
                        Math.random() - 0.5
                ), 4, data);
            }
        }, 0, Material.RED_DYE, "Regular");

        private final Consumer<Player> func;
        private final int id;
        private final String name;
        private final Material icon;

        BloodEffects(Consumer<Player> func, int id, Material icon, String name) {
            this.func = func;
            this.id = id;
            this.icon = icon;
            this.name = name;
        }

        public void perform(Player p) {
            func.accept(p);
        }

        @Override
        public ItemStack icon() {
            return new ItemStack(icon);
        }

        @Override
        public String title() {
            return DARK_RED + BOLD + name;
        }

        @Override
        public String description() {
            return null;
        }

        @Override
        public Integer getItemId() {
            return id;
        }

        @Override
        public boolean select(Player player) {
            if (ownedBy(player)) {
                NetworkMain.getInstance().getProfile(player).setSelectedBloodEffect(this);
            }
            return ownedBy(player);
        }
    }

    public interface Cosmetic extends Purchasable, Selectable, Describable {

        @Override
        default ItemStack icon(Player p) {
            return ownedBy(p) ? icon() : new ItemStack(Material.GRAY_DYE);
        }

        @Override
        default String description(Player p) {
            StringBuilder b = new StringBuilder();
            b.append("\n");
            if (description() != null) {
                b.append(description()).append("\n");
                b.append("\n");
            }
            if (ownedBy(p)) {
                b.append(GREY + "Click to select");
            } else {
                b.append(RED + "Locked");
            }
            return b.toString();
        }

    }
}