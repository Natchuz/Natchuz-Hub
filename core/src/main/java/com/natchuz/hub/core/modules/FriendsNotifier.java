package com.natchuz.hub.core.modules;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import com.natchuz.hub.protocol.messaging.ExchangedClient;
import com.natchuz.hub.protocol.messaging.Protocol;
import com.natchuz.hub.utils.UUIDConverter;
import com.natchuz.hub.core.api.PrivilegedFacade;
import com.natchuz.hub.core.user.User;

import static com.natchuz.hub.paper.Color.*;

/**
 * This module listens on topics related to friends, and handles f.e. join messages
 */
public class FriendsNotifier implements Module {

    private final Protocol protocol;
    private final PrivilegedFacade client;

    public FriendsNotifier(PrivilegedFacade client, Protocol protocol) {
        this.protocol = protocol;
        this.client = client;

        protocol.handle("joinEvent", this::joinMessage);
        protocol.handle("leftEvent", this::leftMessage);
    }

    public void joinMessage(String[] args) {
        User user = client.getProfile(UUIDConverter.fromCondensed(args[0]));

        for (UUID uuid : user.getFriends()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(GREEN + BOLD + "→ " + RESET + user.chatName());
            }
        }
    }

    public void leftMessage(String[] args) {
        User user = client.getProfile(UUIDConverter.fromCondensed(args[0]));

        for (UUID uuid : user.getFriends()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(RED + BOLD + "← " + RESET + user.chatName());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        User user = client.getProfile(event.getPlayer());

        user.getFriends().forEach(u -> ((ExchangedClient) protocol.getClient())
                .subscribe("player.friend." + UUIDConverter.toCondensed(u)));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        User user = client.getProfile(event.getPlayer());

        user.getFriends().forEach(u -> ((ExchangedClient) protocol.getClient())
                .unsubscribe("player.friend." + UUIDConverter.toCondensed(u)));
    }

}
