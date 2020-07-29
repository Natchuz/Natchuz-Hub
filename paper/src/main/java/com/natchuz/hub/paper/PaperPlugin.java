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
import com.natchuz.hub.paper.managers.DialogManager;
import com.natchuz.hub.paper.managers.SidebarManager;

@Plugin(id = "sponge-utils", name = "Sponge Utils", version = "1.0")
public class PaperPlugin {

    @Inject
    private Game game;
    @Inject
    private Logger logger;

    private final HologramsService hologramsService;
    private final DialogManager dialogManager;
    private final SidebarManager sidebarManager;

    public PaperPlugin() {
        hologramsService = new HologramsServiceImpl();
        dialogManager = new DialogManager();
        sidebarManager = new SidebarManager("", "");
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        game.getEventManager().registerListeners(this, hologramsService);
        game.getEventManager().registerListeners(this, dialogManager);
        game.getEventManager().registerListeners(this, sidebarManager);
    }

    @Listener
    public void onPostInit(GamePostInitializationEvent event) {
        game.getServiceManager().setProvider(this, HologramsService.class, hologramsService);
        game.getServiceManager().setProvider(this, DialogManager.class, dialogManager);
        game.getServiceManager().setProvider(this, SidebarManager.class, sidebarManager);
    }

}
