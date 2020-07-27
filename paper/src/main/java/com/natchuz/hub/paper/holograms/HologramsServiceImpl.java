package com.natchuz.hub.paper.holograms;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import java.util.List;

public class HologramsServiceImpl implements HologramsService {

    @Override
    public void add(String id, Location<?> location, List<Text> lines) {

    }

    @Override
    public void remove() {

    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        Sponge.getServer().getPlayer("").ifPresent(p -> {

        });
    }
}
