package com.natchuz.hub.bungeecord.legacy

import com.natchuz.hub.protocol.arch.Services
import com.natchuz.hub.protocol.messaging.Protocol
import com.natchuz.hub.utils.toCondensed
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

class FriendsNotifierSource(private val protocol: Protocol) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: ServerConnectEvent) {
        if (event.reason == ServerConnectEvent.Reason.JOIN_PROXY) {
            val uuid = event.player.uniqueId.toCondensed()
            protocol.send(Services.PAPER.getMessageEndpoint("player.friend.$uuid"), "joinEvent", uuid)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerDisconnectEvent) {
        val uuid = event.player.uniqueId.toCondensed()
        protocol.send(Services.PAPER.getMessageEndpoint("player.friend.$uuid"), "leftEvent", uuid)
    }
}