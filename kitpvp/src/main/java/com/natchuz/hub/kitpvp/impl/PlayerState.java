package com.natchuz.hub.kitpvp.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.natchuz.hub.kitpvp.ConcreteKitGamemode;

/**
 * Represents a state of player in game, and how different events should be handled depending on it.
 * Holds objects common for every state.
 */
@AllArgsConstructor
public abstract class PlayerState {

    @Delegate
    @Getter
    private final ConcreteKitGamemode gamemode;
    @Getter
    private final Player player;

    //region handlers

    public void onMove(PlayerMoveEvent event) {
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
    }

    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    }

    /**
     * Called when player gets damaged
     *
     * @see StateHolderController#onDamage(EntityDamageEvent)
     */
    public void onEntityDamageEvent(EntityDamageEvent event) {
    }

    /**
     * Called when player other entity
     *
     * @see StateHolderController#onEntityDamageEvent(EntityDamageByEntityEvent)
     */
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
    }

    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
    }

    //endregion
}
