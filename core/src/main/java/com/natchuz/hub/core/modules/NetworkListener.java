package com.natchuz.hub.core.modules;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.natchuz.hub.protocol.state.ServerID;
import com.natchuz.hub.protocol.state.StateDatabase;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.user.User;

/**
 * This module increases or decreases player count of this server in state database
 */
public class NetworkListener implements Module {

    private final StateDatabase state;

    public NetworkListener(StateDatabase database) {
        this.state = database;
    }

    //region event handlers

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = NetworkMain.getInstance().getProfile(event.getPlayer());

        state.incrPlayers(ServerID.fromString(System.getenv("SERVERTYPE") + "/" + System.getenv("SERVERID")));

        player.setDisplayName(user.chatName());
        event.setJoinMessage("");
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = NetworkMain.getInstance().getProfile(player);

        state.decrPlayers(ServerID.fromString(System.getenv("SERVERTYPE") + "/" + System.getenv("SERVERID")));

        event.setQuitMessage("");
    }

    //endregion
}
