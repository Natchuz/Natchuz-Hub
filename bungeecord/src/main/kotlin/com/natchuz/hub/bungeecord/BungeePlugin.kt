package com.natchuz.hub.bungeecord

import com.natchuz.hub.protocol.arch.Services
import com.natchuz.hub.protocol.messaging.Protocol
import com.natchuz.hub.protocol.state.JoinFlags
import com.natchuz.hub.protocol.state.RedisStateDatabase
import com.natchuz.hub.protocol.state.StateDatabase
import com.natchuz.hub.utils.VersionInfo
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
import org.apache.commons.lang.StringUtils
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.*
import javax.imageio.ImageIO

/**
 * Entry class for Bungeecord Plugin
 */
class BungeeMain : Plugin(), Listener {

    private val landingServerType = "lb"

    private lateinit var protocol: Protocol
    private lateinit var favicon: Favicon
    private lateinit var playersPing: Players
    private lateinit var motd: TextComponent
    private lateinit var state: StateDatabase

    override fun onLoad() {
        protocol = Protocol(Services.BUNGEECORD.createClient())

        playersPing = Players(5000, proxy.onlineCount, emptyArray())
        favicon = Favicon.create(ImageIO.read(getResourceAsStream("logo.png")))

        val version = VersionInfo(javaClass)
        motd = TextComponent()
        motd.extra = Arrays.asList(*TextComponent.fromLegacyText("""             §e§lNatchuz§4 §lHub§r§8 | §7Best §r§a1.14 §r§7server§r§b§l!
            §r§f${StringUtils.center(version.display, 58)}"""))

        state = RedisStateDatabase("redis")
    }

    override fun onEnable() {
        proxy.pluginManager.registerListener(this, this)
        proxy.pluginManager.registerListener(this, FriendsNotifierSource(protocol))

        protocol.handle("connect") {
            proxy.servers[it[0]] = proxy.constructServerInfo(it[0],
                    InetSocketAddress(InetAddress.getByName(it[0]), 25565), "Hello world!", false)
        }

        protocol.handle("disconnect") {
            proxy.servers.remove(it[0])
        }

        protocol.handle("send") {
            val player = proxy.getPlayer(it[0]) ?: return@handle
            val freeServer = state.getServers(it[1]).get().minWith(Comparator.comparingInt { obj -> obj.players })

            if (freeServer != null) { // when there was at least one server
                val server = proxy.getServerInfo(freeServer.id.id)

                state.setPlayerJoinFlags(player.uniqueId,
                        *it.sliceArray((2 until it.size)).map { i -> JoinFlags.valueOf(i) }.toTypedArray())

                player.sendMessage(*ComponentBuilder("Sending you to ${server.name}")
                        .color(ChatColor.GREEN).create())

                player.connect(server, { success, _ ->
                    if (!success)
                        player.sendMessage(*ComponentBuilder("Could not send you to server ${server.name}")
                                .color(ChatColor.RED).create())
                }, ServerConnectEvent.Reason.PLUGIN)
            } else { // when there was no server
                player.sendMessage(*ComponentBuilder("No servers of this type are running right now")
                        .color(ChatColor.RED)
                        .create())
            }
        }

        protocol.handle("kill") {
            proxy.players.forEach { player ->
                player.disconnect(TextComponent("the party is over"))
            }
        }

        protocol.startReceiving()
    }

    @EventHandler
    fun onLeave(event: PlayerDisconnectEvent) {
        state.dropPlayer(event.player.uniqueId)
    }

    @EventHandler
    fun onChange(event: ServerConnectedEvent) {
        state.setPlayerLocation(event.player.uniqueId, event.server.info.name)
    }

    @EventHandler
    fun onJoin(event: ServerConnectEvent) {
        if (event.reason == ServerConnectEvent.Reason.JOIN_PROXY) {
            state.setPlayerJoinFlags(event.player.uniqueId, JoinFlags.PROXY_JOIN)
            state.getServers(landingServerType).get().stream()
                    .min(Comparator.comparingInt { it.players })
                    .ifPresent { event.target = proxy.getServerInfo(it.id.id) }
        }
    }

    @EventHandler
    fun onPing(event: ProxyPingEvent) {
        event.response.descriptionComponent = motd
        event.response.players = playersPing
        event.response.setFavicon(favicon)
    }
}