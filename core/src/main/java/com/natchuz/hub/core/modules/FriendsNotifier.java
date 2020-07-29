package com.natchuz.hub.core.modules;

import com.natchuz.hub.protocol.messaging.Protocol;

/**
 * This module listens on topics related to friends, and handles f.e. join messages
 */
public class FriendsNotifier implements Module {

    private Protocol protocol;

    public FriendsNotifier(Protocol protocol) {
        protocol.handle("joinEvent", this::joinMessage);
        protocol.handle("leftEvent", this::leftMessage);
    }

    public void joinMessage(String[] args) {
        /*User user = client.getProfile(UUIDConverter.fromCondensed(args[0]));

        for (UUID uuid : user.getFriends()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(GREEN + BOLD + "→ " + RESET + user.chatName());
            }
        }*/
    }

    public void leftMessage(String[] args) {
        /*User user = client.getProfile(UUIDConverter.fromCondensed(args[0]));

        for (UUID uuid : user.getFriends()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(RED + BOLD + "← " + RESET + user.chatName());
            }
        }*/
    }

    public void onPlayerJoin() {
        /*User user = client.getProfile(event.getPlayer());

        user.getFriends().forEach(u -> ((ExchangedClient) protocol.getClient())
                .subscribe("player.friend." + UUIDConverter.toCondensed(u)));*/
    }

    public void onPlayerQuit() {
        /*User user = client.getProfile(event.getPlayer());

        user.getFriends().forEach(u -> ((ExchangedClient) protocol.getClient())
                .unsubscribe("player.friend." + UUIDConverter.toCondensed(u)));*/
    }

}
