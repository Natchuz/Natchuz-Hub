package com.natchuz.hub.core.modules;

import com.natchuz.hub.protocol.messaging.Protocol;

public class ProtocolHandler implements Module {

    //private PrivilegedFacade facade;
    private Protocol protocol;

    public ProtocolHandler() {
        //this.facade = facade;
        //this.protocol = protocol;
    }

    public void serverEnable() {
        /*if (enableEvent.getPlugin().getClass() == NetworkMain.class) {
            protocol.handle("kill", (s) -> Bukkit.shutdown());
            ((ExchangedClient) protocol.getClient()).subscribe("server." + facade.getServerId());
            protocol.startReceiving();
            protocol.send(Services.BUNGEECORD.messageEndpoint(), "connect", facade.getServerId());
        }*/
    }

    //@SneakyThrows
    //@EventHandler
    public void serverDisable() {
        /*if (enableEvent.getPlugin().getClass() == NetworkMain.class) {
            protocol.send(Services.BUNGEECORD.messageEndpoint(), "disconnect", facade.getServerId());
        }*/
    }

}
