package com.natchuz.hub.lobby;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.natchuz.hub.paper.serialization.LocationDeserializer;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.api.MainFacade;

@Plugin(id = "natchuz-hub-lobby")
public class HubPlugin {

    @Listener
    public void onReady(GameStartingServerEvent event) {

        MainFacade core = (NetworkMain) Sponge.getPluginManager().getPlugin("core-plugin").get()
                .getInstance().get();

        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Location<World>>() {}.getType(),
                new LocationDeserializer<World>(Sponge.getServer().getWorld(core.getMapWorld()).get())).create();
        //new LocationDeserializer<World>(Sponge.getServer().getWorlds().stream().findAny().get())).create();

        MapConfig mapConfig = gson.fromJson(core.getMapConfiguration(), MapConfig.class);

        Sponge.getEventManager().registerListeners(this, new LobbyListener(mapConfig));
    }
}
