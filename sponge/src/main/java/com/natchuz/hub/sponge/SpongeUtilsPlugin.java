package com.natchuz.hub.sponge;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import com.natchuz.hub.sponge.holograms.HologramsService;
import com.natchuz.hub.sponge.holograms.HologramsServiceImpl;
import com.natchuz.hub.sponge.managers.DialogManager;
import com.natchuz.hub.sponge.managers.SidebarManager;

@Plugin(id = "natchuz-hub-sponge-utils", name = "Sponge Utils", version = "1.0")
public class SpongeUtilsPlugin {

    @Inject
    private Game game;
    @Inject
    private Logger logger;

    private final HologramsService hologramsService;
    private final DialogManager dialogManager;
    private final SidebarManager sidebarManager;

    public SpongeUtilsPlugin() {
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
