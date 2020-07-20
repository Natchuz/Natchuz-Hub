package com.natchuz.hub.kitpvp.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

/**
 * This listener invokes event handlers on {@link PlayerState states} stored in {@link StateHolder StateHolder}
 */
public class StateHolderController implements Listener {

    private final StateHolder holder;

    public StateHolderController(StateHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void onPlayerLeftEvent(PlayerQuitEvent event) {
        holder.removeState(event.getPlayer()); // cleanup
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        holder.getState(event.getPlayer()).onPlayerInteractEntity(event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        holder.getState(event.getPlayer()).onPlayerInteract(event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        holder.getState(event.getPlayer()).onMove(event);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            holder.getState((Player) event.getEntity()).onEntityDamageEvent(event);
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            holder.getState((Player) event.getDamager()).onEntityDamageByEntityEvent(event);
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        holder.getState(event.getPlayer()).onPlayerInteractAtEntityEvent(event);
    }
}
