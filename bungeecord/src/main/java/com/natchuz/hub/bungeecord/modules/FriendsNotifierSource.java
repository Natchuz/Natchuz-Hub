package com.natchuz.hub.bungeecord.modules;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import com.natchuz.hub.protocol.arch.Services;
import com.natchuz.hub.protocol.messaging.Protocol;
import com.natchuz.hub.utils.UUIDConverter;

public class FriendsNotifierSource implements Listener {

    private final Protocol protocol;

    public FriendsNotifierSource(Protocol protocol) {
        this.protocol = protocol;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(ServerConnectEvent event) {
        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            String uuid = UUIDConverter.toCondensed(event.getPlayer().getUniqueId());

            protocol.send(Services.PAPER.getMessageEndpoint("player.friend."
                    + uuid), "joinEvent", uuid);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        String uuid = UUIDConverter.toCondensed(event.getPlayer().getUniqueId());

        protocol.send(Services.PAPER.getMessageEndpoint("player.friend."
                + uuid), "leftEvent", uuid);
    }
}
