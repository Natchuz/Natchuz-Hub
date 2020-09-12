package com.natchuz.hub.core;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.Objects;

import com.natchuz.hub.backend.state.PlayerFlags;
import com.natchuz.hub.core.api.proxy.ProxyBackend;

public class StandaloneProxyBackend implements ProxyBackend {

    @Override
    public void send(String name, String target, PlayerFlags... flags) {
        send(Objects.requireNonNull(Sponge.getGame().getServer().getPlayer(name).get()), target, flags);
    }

    @Override
    public void send(Player player, String target, PlayerFlags... flags) {
        player.sendMessage(Text.of(String.format("[DEBUG] You were supposed to be sent to %s with %s flags, "
                        + "but we're running in standalone context!", target, Arrays.toString(flags)), TextColors.RED));
    }
}
