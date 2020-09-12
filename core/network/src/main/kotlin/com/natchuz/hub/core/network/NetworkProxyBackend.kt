package com.natchuz.hub.core.network

import com.natchuz.hub.backend.state.PlayerFlags
import com.natchuz.hub.backend.state.SendRequest
import com.natchuz.hub.core.api.proxy.ProxyBackend
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player

class NetworkProxyBackend(
        private val client: HttpClient
) : ProxyBackend {

    override fun send(name: String, target: String, vararg flags: PlayerFlags) {
        send(Sponge.getServer().getPlayer(name).get(), target, *flags)
    }

    override fun send(player: Player, target: String, vararg flags: PlayerFlags) = runBlocking {
        client.post<Unit>("http://state/player/${player.uniqueId}/send",
                body = SendRequest(target, flags.asList()))
    }
}