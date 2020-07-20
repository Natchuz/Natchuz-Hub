package com.natchuz.hub.core.proxy;

import org.bukkit.entity.Player;

import com.natchuz.hub.protocol.arch.Services;
import com.natchuz.hub.protocol.messaging.Protocol;
import com.natchuz.hub.protocol.state.JoinFlags;

public class NetworkProxyBackend implements ProxyBackend {

    private final Protocol protocol;

    public NetworkProxyBackend(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public void send(String name, String target, JoinFlags... flags) {
        String[] params = new String[2 + flags.length];
        params[0] = name;
        params[1] = target;

        for (int i = 0; i < flags.length; i++)
            params[i + 2] = flags[i].toString();

        protocol.send(Services.BUNGEECORD.messageEndpoint(), "send", params);
    }

    @Override
    public void send(Player player, String target, JoinFlags... flags) {
        send(player.getName(), target, flags);
    }
}
