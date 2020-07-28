package com.natchuz.hub.paper;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import com.natchuz.hub.paper.holograms.HologramsService;
import com.natchuz.hub.paper.holograms.HologramsServiceImpl;

@Plugin(id = "sponge-utils", name = "Sponge Utils", version = "1.0")
public class PaperPlugin {

    @Inject
    private Game game;
    @Inject
    private Logger logger;

    private final HologramsService hologramsService;

    public PaperPlugin() {
        hologramsService = new HologramsServiceImpl();
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        game.getEventManager().registerListeners(this, hologramsService);
    }

    @Listener
    public void onPostInit(GamePostInitializationEvent event) {
        game.getServiceManager().setProvider(this, HologramsService.class, hologramsService);
    }

}
