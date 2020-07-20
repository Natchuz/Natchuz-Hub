package com.natchuz.hub.core.content.cosmetics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.HashMap;

import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.user.User;

/**
 * Handles all passive cosmetics
 */
public class CosmeticsListener implements Listener {

    //region event handlers

    //region projectiles trait

    private final HashMap<Projectile, Integer> projectiles = new HashMap<>();

    @EventHandler
    public void onThrow(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        User user = NetworkMain.getInstance().getProfile((Player) event.getEntity().getShooter());
        if (user.getSelectedTrait().getParticle() == null) return;

        projectiles.put(event.getEntity(), Bukkit.getScheduler().scheduleSyncRepeatingTask(NetworkMain.getInstance().getPlugin(), () -> {
            event.getEntity().getWorld().spawnParticle(user.getSelectedTrait().getParticle(), event.getEntity().getLocation(), 1);
        }, 0, 1));
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (projectiles.get(event.getEntity()) != null)
            Bukkit.getScheduler().cancelTask(projectiles.remove(event.getEntity()));
    }

    @EventHandler
    public void onFishRodRemove(PlayerFishEvent event) {
        if (projectiles.get(event.getHook()) != null)
            Bukkit.getScheduler().cancelTask(projectiles.remove(event.getHook()));
    }

    @EventHandler
    public void onItemChange(PlayerItemHeldEvent event) {
        if (event.getPlayer().getInventory().getItem(event.getPreviousSlot()) != null &&
                event.getPlayer().getInventory().getItem(event.getPreviousSlot()).getType().equals(Material.FISHING_ROD)) {
            for (Projectile p : projectiles.keySet()) {
                if (p.getShooter() == event.getPlayer()) {
                    Bukkit.getScheduler().cancelTask(projectiles.remove(p));
                }
            }
        }
    }

    //endregion
    //region blood effects

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        NetworkMain.getInstance().getProfile(event.getEntity().getUniqueId())
                .getSelectedBloodEffect().perform((Player) event.getEntity());
    }

    //endregion

    //endregion
}
