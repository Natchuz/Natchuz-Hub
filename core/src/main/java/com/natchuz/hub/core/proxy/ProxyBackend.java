package com.natchuz.hub.core.proxy;

import org.spongepowered.api.entity.living.player.Player;

import com.natchuz.hub.protocol.state.JoinFlags;

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
    void send(String name, String target, JoinFlags... flags);

    /**
     * Sends player using his nick
     *
     * @see ProxyBackend#send(String, String, JoinFlags...)
     */
    void send(Player player, String target, JoinFlags... flags);

}
