package com.natchuz.hub.kitpvp.impl;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.natchuz.hub.paper.regions.BlockVectors;
import com.natchuz.hub.kitpvp.ConcreteKitGamemode;
import com.natchuz.hub.kitpvp.map.ArenaSpawn;
import com.natchuz.hub.kitpvp.model.ArenaStats;
import com.natchuz.hub.kitpvp.model.KitProfile;
import com.natchuz.hub.kitpvp.model.content.Kit;

import static com.natchuz.hub.paper.Color.*;

/**
 * Arena state when player is on arena
 */
@ToString
public class ArenaState extends PlayerState {

    private final Arena arena;
    @Getter
    private final ArenaStats stats;
    private int currentStreak;
    private int streakTask;

    private ArenaState(ConcreteKitGamemode plugin, Player player, Arena arena) {
        super(plugin, player);
        this.arena = arena;
        stats = new ArenaStats();
    }

    public void addKill() {
        getStats().addKill();
        currentStreak++;
        getStats().setBestStreaks(Math.max(getStats().getBestStreaks(), currentStreak));

        if (streakTask != -1)
            Bukkit.getScheduler().cancelTask(streakTask);

        streakTask = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
            currentStreak = 0;
            streakTask = -1;
        }, 100);

        StringBuilder builder = new StringBuilder();

        switch (currentStreak) {
            case 1:
                builder.append(RED);
            case 2:
            case 3:
                builder.append(YELLOW);
            default:
                builder.append(GREEN);
        }
        builder.append("x").append(currentStreak);
        getPlayer().sendTitle("", builder.toString(), 5, 75, 20);
    }

    private String[] scoreboardHandler(Player p) {
        String[] ret = new String[2];
        ret[0] = "Coins: " + GOLD + getProfile(p).getCoins();
        ret[1] = "Kills: " + GREEN + getStats().getKills();
        return ret;
    }

    @Override
    public void onEntityDamageEvent(EntityDamageEvent event) {
        /*
            To avoid tight code, let's handle all damage events in one listener
         */

        Player target = (Player) event.getEntity(); // get target

        boolean byEntity = event instanceof EntityDamageByEntityEvent; // if damage was dealt by entity
        EntityDamageByEntityEvent byEntityEvent = byEntity ? (EntityDamageByEntityEvent) event : null;

        Player damager = null;

        // receive attacker if damage was dealt by Player, or Projectile
        if (byEntity && byEntityEvent.getDamager() instanceof Projectile) {
            damager = (Player) ((Projectile) byEntityEvent.getDamager()).getShooter();
        } else if (byEntity) {
            damager = (Player) byEntityEvent.getDamager();
        }

        // handle death
        if (target.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true); // cancel event before death
            KitProfile targetProfile = getProfile(target);

            // default death sound
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
            // drop items
            target.getInventory().forEach(i -> {
                if (i != null) target.getWorld().dropItemNaturally(target.getLocation(), i);
            });

            target.setHealth(20); // heal target

            targetProfile.getStats().addStats(stats);

            LobbyState.beginNewState(getGamemode(), getPlayer(), getMainLobby());

            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);

            if (byEntity) {
                damager.getInventory()
                        .addItem(new ItemStack(Material.GOLDEN_APPLE), new ItemStack(Material.LAPIS_LAZULI))
                        .forEach((in, is) -> target.getWorld().dropItemNaturally(target.getLocation(), is));

                KitProfile damagerProfile = getProfile(damager);
                damagerProfile.setCoins(damagerProfile.getCoins() + 20);
                updateProfile(damagerProfile);

                damager.setHealth(Math.min(damager.getHealth() + 10,
                        damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));

                ArenaState damagerState = (ArenaState) getState(damager);
                damagerState.addKill();

                getSidebarManager().refresh(damager);
                getSidebarManager().refresh(target);

                Bukkit.broadcastMessage(target.getDisplayName() + GREY + " got killed by " + RESET
                        + damager.getDisplayName()
                        + RESET + GREY + " with " + RESET + WHITE + (int) damager.getHealth() + DARK_RED + "❤");
            } else {
                Bukkit.broadcastMessage(target.getDisplayName() + RESET + GREY + " is now dead.");
            }
        }
    }

    @Override
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getTo().getY() < arena.getVoidHeight()) {
            Bukkit.getPluginManager().callEvent(
                    new EntityDamageEvent(player, EntityDamageEvent.DamageCause.VOID, 1000));
        }
    }

    public static void beginArenaState(ConcreteKitGamemode plugin, Player player,
                                       Arena arena, ArenaSpawn spawn, Kit kit) {
        ArenaState state = new ArenaState(plugin, player, arena);

        player.teleport(BlockVectors.centerFlat(spawn.getLocation()));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        InventoryUtils.applySet(kit.getContent(), player);

        for (PotionEffect e : player.getActivePotionEffects()) {
            player.removePotionEffect(e.getType());
        }

        player.addPotionEffect(new PotionEffect(
                        PotionEffectType.SPEED,
                        100,
                        0,
                        false,
                        false,
                        true),
                true);
        player.addPotionEffect(new PotionEffect(
                        PotionEffectType.DAMAGE_RESISTANCE,
                        100,
                        3,
                        false,
                        false,
                        true),
                true);

        plugin.getSidebarManager().set(player, state::scoreboardHandler);

        plugin.initNewState(player, state);
    }
}
