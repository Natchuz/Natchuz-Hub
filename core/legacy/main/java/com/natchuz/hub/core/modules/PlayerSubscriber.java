package com.natchuz.hub.core.modules;

import com.natchuz.hub.protocol.messaging.Protocol;

/**
 * This player subscribes to player-related messages on network
 */
public class PlayerSubscriber implements Module {

    private final Protocol protocol;

    public PlayerSubscriber(Protocol protocol) {
        this.protocol = protocol;
    }

    public void onJoin() {
        //((ExchangedClient) protocol.getClient()).subscribe("player." + event.getPlayer().getUniqueId().toString());
    }

    public void onLeave() {
        //((ExchangedClient) protocol.getClient()).unsubscribe("player." + event.getPlayer().getUniqueId().toString());
    }
}
