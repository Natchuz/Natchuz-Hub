package com.natchuz.hub.core.api.proxy;

import org.spongepowered.api.entity.living.player.Player;

import com.natchuz.hub.backend.state.PlayerFlags;

/**
 * Provides client for servers proxy
 */
public interface ProxyBackend {

    /**
     * Sends a specified player to certain server
     *
     * @param name   nick of player
     * @param target server name
     * @param flags  list of flags to send a player with
     */
    void send(String name, String target, PlayerFlags... flags);

    /**
     * Sends player using his nick
     *
     * @see ProxyBackend#send(String, String, PlayerFlags...)
     */
    void send(Player player, String target, PlayerFlags... flags);

}
