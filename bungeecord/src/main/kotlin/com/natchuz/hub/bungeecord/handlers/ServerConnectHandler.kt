package com.natchuz.hub.bungeecord.handlers

import com.natchuz.hub.backend.state.PlayerLoginStatus
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class ServerConnectHandler(
        private val backendClient: HttpClient,
        private val proxy: ProxyServer,
) : Listener {

    @EventHandler
    fun onJoin(event: ServerConnectEvent) = runBlocking {
        when (event.reason) {
            ServerConnectEvent.Reason.JOIN_PROXY -> handleJoinProxy(event)
            else -> Unit
        }
    }

    private suspend fun handleJoinProxy(event: ServerConnectEvent) = with(event) {
        val response = backendClient.post<PlayerLoginStatus> {
            url("http://state/player/${player.uniqueId}/login")
            timeout {
                player.disconnect("Could not connect!")
            }
        }

        when (response) {
            is PlayerLoginStatus.Ok -> {
                val serverInfo = proxy.getServerInfo(response.targetServer)
                if (serverInfo == null) {
                    player.disconnect("Error while connecting you to the server")
                    return
                }
                target = proxy.getServerInfo(response.targetServer)
            }
            is PlayerLoginStatus.Ban -> {
                player.disconnect(response.reason)
            }
            else -> Unit
        }
    }

}