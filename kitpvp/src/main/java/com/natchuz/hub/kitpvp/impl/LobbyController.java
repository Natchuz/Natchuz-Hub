package com.natchuz.hub.kitpvp.impl;

import org.apache.commons.lang.Validate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

/**
 * This is event handler for {@link Lobby lobby}.
 * For example, cancelling damaging NPCs, since they are immutable no matter of state.
 */
public class LobbyController implements Listener {

    private final Lobby handle;

    /**
     * @param handle Lobby which to control
     */
    public LobbyController(Lobby handle) {
        Validate.notNull(handle, "Lobby cannot be null!");
        this.handle = handle;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        UUID entity = event.getEntity().getUniqueId();

        if (entity.equals(handle.getHubNpc().getUniqueId())
                || handle.getArmorStands().stream()
                .anyMatch(h -> h.getUniqueId().equals(entity))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityBurn(EntityCombustEvent event) {
        UUID entity = event.getEntity().getUniqueId();

        if (entity.equals(handle.getHubNpc().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
