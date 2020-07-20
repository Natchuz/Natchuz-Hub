package com.natchuz.hub.bungeecord;

import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;

import javax.imageio.ImageIO;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;

import com.natchuz.hub.protocol.arch.Services;
import com.natchuz.hub.protocol.messaging.Protocol;
import com.natchuz.hub.protocol.state.JoinFlags;
import com.natchuz.hub.protocol.state.RedisStateDatabase;
import com.natchuz.hub.protocol.state.Server;
import com.natchuz.hub.protocol.state.StateDatabase;
import com.natchuz.hub.utils.VersionInfo;
import com.natchuz.hub.bungeecord.modules.FriendsNotifierSource;

/**
 * This is the main class of bungeecord plugin
 */
public class BungeeMain extends Plugin implements Listener {

    private final String UNDER_MAINTENANCE = "§b§lServers under maintenance!§r\n\nTry again in a second!";
    private final String landingServerType = "lb";

    private Jedis jedis;
    private Protocol protocol;
    private Favicon favicon;
    private ServerPing.Players playersPing;
    private TextComponent motd;

    private StateDatabase state;

    @SneakyThrows
    @Override
    public void onLoad() {
        this.jedis = new Jedis("redis");
        this.protocol = new Protocol(Services.BUNGEECORD.createClient());

        VersionInfo version = new VersionInfo(getClass());

        this.playersPing = new ServerPing.Players(5000, getProxy().getOnlineCount(), new ServerPing.PlayerInfo[0]);
        this.favicon = Favicon.create(ImageIO.read(getResourceAsStream("logo.png")));
        this.motd = new TextComponent();
        this.motd.setExtra(Arrays.asList(TextComponent.fromLegacyText("             §e§lNatchuz§4 §lHub§r§8 | §7Best §r§a1.14 §r§7server§r§b§l!\n§r§f" +
                StringUtils.center(version.getDisplay(), 58))));

        state = new RedisStateDatabase("redis");
    }

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerListener(this, new FriendsNotifierSource(protocol));

        protocol.handle("connect", (s) -> {
            ServerInfo info = null;
            try {
                info = getProxy().constructServerInfo(s[0], new InetSocketAddress(InetAddress.getByName(s[0]), 25565), "Hello world!", false);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            getProxy().getServers().put(s[0], info);
        });

        protocol.handle("disconnect", (s) -> {
            getProxy().getServers().remove(s[0]);
        });

        protocol.handle("send", (s) -> {

            Optional<Server> freeServer = state.getServers(s[1]).get().stream()
                    .min(Comparator.comparingInt(Server::getPlayers));

            ProxiedPlayer player = getProxy().getPlayer(s[0]);
            if (player == null)
                return;

            if (freeServer.isPresent()) {
                ServerInfo server = getProxy().getServerInfo(freeServer.get().getId().getId());

                if (server == null)
                    return;

                state.setPlayerJoinFlags(player.getUniqueId(), IntStream.range(2, s.length)
                        .mapToObj(i -> JoinFlags.valueOf(s[i]))
                        .toArray(JoinFlags[]::new));

                TextComponent sendingText = new TextComponent("Sending you to " + server.getName());
                sendingText.setColor(ChatColor.GREEN);
                player.sendMessage(sendingText);

                player.connect(server, (success, error) -> {
                    if (!success) {
                        TextComponent failureText =
                                new TextComponent("Could not send you to server " + server.getName());
                        failureText.setColor(ChatColor.RED);
                        player.sendMessage(failureText);
                    }
                }, ServerConnectEvent.Reason.PLUGIN);

            } else {
                TextComponent errorMessage = new TextComponent("No servers of this type are running right now");
                errorMessage.setColor(ChatColor.RED);
                player.sendMessage(errorMessage);
            }
        });

        protocol.handle("kill", (s) -> {
            getProxy().getPlayers().forEach((p) -> {
                p.disconnect(new TextComponent("the party is over"));
            });
        });

        protocol.startReceiving();
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        state.dropPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChange(ServerConnectedEvent event) {
        state.setPlayerLocation(event.getPlayer().getUniqueId(), event.getServer().getInfo().getName());
    }

    @SneakyThrows
    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        state.getServers(landingServerType).thenAccept(t -> {
            if (t.size() == 0) {
                event.setCancelled(true);
                event.setCancelReason(TextComponent.fromLegacyText(UNDER_MAINTENANCE));
            }
        }).get();
    }

    @SneakyThrows
    @EventHandler
    public void onJoin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            state.setPlayerJoinFlags(player.getUniqueId(), JoinFlags.PROXY_JOIN);

            state.getServers(landingServerType).get().stream()
                    .min(Comparator.comparingInt(Server::getPlayers))
                    .ifPresent(t -> event.setTarget(getProxy().getServerInfo(t.getId().getId())));
        }
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        event.getResponse().setDescriptionComponent(motd);
        event.getResponse().setPlayers(playersPing);
        event.getResponse().setFavicon(favicon);
    }
}
