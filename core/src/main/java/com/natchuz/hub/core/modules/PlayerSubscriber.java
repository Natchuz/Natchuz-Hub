package com.natchuz.hub.core.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.natchuz.hub.protocol.messaging.ExchangedClient;
import com.natchuz.hub.protocol.messaging.Protocol;

/**
 * This player subscribes to player-related messages on network
 */
public class PlayerSubscriber implements Module {

    private final Protocol protocol;

    public PlayerSubscriber(Protocol protocol) {
        this.protocol = protocol;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ((ExchangedClient) protocol.getClient()).subscribe("player." + event.getPlayer().getUniqueId().toString());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        ((ExchangedClient) protocol.getClient()).unsubscribe("player." + event.getPlayer().getUniqueId().toString());
    }
}
