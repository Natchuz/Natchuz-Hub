package com.natchuz.hub.core.modules;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import com.natchuz.hub.protocol.arch.Services;
import com.natchuz.hub.protocol.messaging.ExchangedClient;
import com.natchuz.hub.protocol.messaging.Protocol;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.api.PrivilegedFacade;

public class ProtocolHandler implements Module {

    private final PrivilegedFacade facade;
    private final Protocol protocol;

    public ProtocolHandler(PrivilegedFacade facade, Protocol protocol) {
        this.facade = facade;
        this.protocol = protocol;
    }

    @EventHandler
    public void serverEnable(PluginEnableEvent enableEvent) {
        if (enableEvent.getPlugin().getClass() == NetworkMain.class) {
            protocol.handle("kill", (s) -> Bukkit.shutdown());
            ((ExchangedClient) protocol.getClient()).subscribe("server." + facade.getServerId());
            protocol.startReceiving();
            protocol.send(Services.BUNGEECORD.messageEndpoint(), "connect", facade.getServerId());
        }
    }

    @SneakyThrows
    @EventHandler
    public void serverDisable(PluginDisableEvent enableEvent) {
        if (enableEvent.getPlugin().getClass() == NetworkMain.class) {
            protocol.send(Services.BUNGEECORD.messageEndpoint(), "disconnect", facade.getServerId());
        }
    }

}
