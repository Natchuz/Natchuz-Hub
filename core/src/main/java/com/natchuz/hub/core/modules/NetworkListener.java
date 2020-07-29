package com.natchuz.hub.core.modules;

/**
 * This module increases or decreases player count of this server in state database
 */
public class NetworkListener implements Module {

    //region event handlers

    private void onPlayerJoin() {
        /*Player player = event.getPlayer();
        User user = NetworkMain.getInstance().getProfile(event.getPlayer());

        state.incrPlayers(ServerID.fromString(System.getenv("SERVERTYPE") + "/" + System.getenv("SERVERID")));

        player.setDisplayName(user.chatName());
        event.setJoinMessage("");
    }

    private void onPlayerQuit() {
        Player player = event.getPlayer();
        User user = NetworkMain.getInstance().getProfile(player);

        state.decrPlayers(ServerID.fromString(System.getenv("SERVERTYPE") + "/" + System.getenv("SERVERID")));

        event.setQuitMessage("");*/
    }

    //endregion
}
