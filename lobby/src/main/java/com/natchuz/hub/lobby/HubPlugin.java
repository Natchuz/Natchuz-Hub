package com.natchuz.hub.lobby;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.natchuz.hub.sponge.serialization.LocationDeserializer;
import com.natchuz.hub.core.CorePlugin;
import com.natchuz.hub.core.api.MainFacade;

@Plugin(id = "natchuz-hub-lobby", dependencies = {@Dependency(id="natchuz-hub-core")})
public class HubPlugin {

    @Listener
    public void onReady(GameStartedServerEvent event) {

        MainFacade core = (CorePlugin) Sponge.getPluginManager().getPlugin("natchuz-hub-core").get()
                .getInstance().get();

        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Location<World>>() {}.getType(),
                new LocationDeserializer<World>(Sponge.getServer().getWorld(core.getMapWorld()).get())).create();
        //new LocationDeserializer<World>(Sponge.getServer().getWorlds().stream().findAny().get())).create();

        MapConfig mapConfig = gson.fromJson(core.getMapConfiguration(), MapConfig.class);

        Sponge.getEventManager().registerListeners(this, new LobbyListener(mapConfig));
    }
}
