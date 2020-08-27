package com.natchuz.hub.bungeecord

import com.natchuz.hub.backend.state.PlayerLoginStatus
import com.natchuz.hub.bungeecord.handlers.ServerConnectHandler
import com.natchuz.hub.bungeecord.legacy.FriendsNotifierSource
import com.natchuz.hub.protocol.arch.Services
import com.natchuz.hub.protocol.messaging.Protocol
import com.natchuz.hub.utils.VersionInfo
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.modules.SerializersModule
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.Favicon
import net.md_5.bungee.api.ServerPing.Players
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.*
import javax.imageio.ImageIO
import kotlinx.serialization.json.Json as JsonBuilder

/**
 * Entry class for Bungeecord Plugin
 */
class BungeeMain : Plugin(), Listener {

    private lateinit var protocol: Protocol
    private lateinit var favicon: Favicon
    private lateinit var playersPing: Players
    private lateinit var motd: TextComponent
    private lateinit var backendClient: HttpClient
    private lateinit var serverConnectHandler: ServerConnectHandler

    override fun onLoad() {
        backendClient = HttpClient {
            installSerializers()
            install(HttpTimeout) {
                requestTimeoutMillis = 1000
            }
        }

        serverConnectHandler = ServerConnectHandler(backendClient, proxy)

        protocol = Protocol(Services.BUNGEECORD.createClient())

        playersPing = Players(5000, proxy.onlineCount, emptyArray())
        favicon = Favicon.create(ImageIO.read(getResourceAsStream("logo.png")))

        val version = VersionInfo(this::class)
        motd = TextComponent()
        motd.extra = listOf(*TextComponent.fromLegacyText("""             §e§lNatchuz§4 §lHub§r§8 | §7Best §r§a1.14 §r§7server§r§b§l!
            |§r§f${version.display}""".trimMargin("|")))
    }

    override fun onEnable() {
        proxy.pluginManager.registerListener(this, this)
        proxy.pluginManager.registerListener(this, serverConnectHandler)
        //proxy.pluginManager.registerListener(this, FriendsNotifierSource(protocol))

        protocol.handle("connect") {
            proxy.servers[it[0]] = proxy.constructServerInfo(it[0],
                    InetSocketAddress(InetAddress.getByName(it[0]), 25565), "Hello world!", false)
        }

        proxy.servers["lobby"] = proxy.constructServerInfo("lobby",
                InetSocketAddress(InetAddress.getByName("lobby"), 25565), "Hello world!", false)

        protocol.handle("disconnect") {
            proxy.servers.remove(it[0])
        }

        protocol.handle("send") {
            val player = proxy.getPlayer(UUID.fromString(it[0])) ?: return@handle
            val server = proxy.getServerInfo(it[1])

            player.sendMessage(*ComponentBuilder("Sending you to ${server.name}")
                    .color(ChatColor.GREEN).create())

            player.connect(server, { success, _ ->
                if (!success)
                    player.sendMessage(*ComponentBuilder("Could not send you to server ${server.name}")
                            .color(ChatColor.RED).create())
            }, ServerConnectEvent.Reason.PLUGIN)
        }

        protocol.handle("kill") {
            proxy.players.forEach { player ->
                player.disconnect(TextComponent("the party is over"))
            }
        }

        protocol.startReceiving()
    }

    @EventHandler
    fun onLeave(event: PlayerDisconnectEvent): Unit = runBlocking {
        backendClient.post("http://state/player/${event.player.uniqueId}/logout")
    }

    //@EventHandler
    //fun onChange(event: ServerConnectedEvent): Unit = TODO("inform backend about change")

    @EventHandler
    fun onPing(event: ProxyPingEvent) = with(event.response) {
        descriptionComponent = motd
        players = playersPing
        setFavicon(favicon)
    }
}

internal fun HttpClientConfig<*>.installSerializers() {
    install(JsonFeature) {
        serializer = KotlinxSerializer(JsonBuilder {
            serializersModule = SerializersModule {
                contextual(PlayerLoginStatus::class, PlayerLoginStatus.Serializer)
            }
        })
    }
}