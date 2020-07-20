package com.natchuz.hub.core.proxy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

import com.natchuz.hub.protocol.state.JoinFlags;

import static com.natchuz.hub.paper.Color.*;

public class StandaloneProxyBackend implements ProxyBackend {

    @Override
    public void send(String name, String target, JoinFlags... flags) {
        send(Objects.requireNonNull(Bukkit.getPlayer(name)), target, flags);
    }

    @Override
    public void send(Player player, String target, JoinFlags... flags) {
        player.sendMessage(String.format(RED + "[DEBUG] " + DARK_AQUA
                        + "You were supposed to be sent to %s with %s flags, "
                        + "but we're running in standalone context!",
                RED + target + DARK_AQUA, YELLOW + Arrays.toString(flags) + DARK_AQUA));
    }
}
