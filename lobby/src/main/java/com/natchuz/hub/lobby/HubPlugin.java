package com.natchuz.hub.lobby;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.natchuz.hub.paper.serialization.LocationDeserializer;

@Plugin(id = "natchuz-hub-lobby")
public class HubPlugin {

    @Listener
    public void onReady() {

        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Location<World>>() {}.getType(),
                //new LocationDeserializer<World>(networkClient.getMapWorld())).create();
                new LocationDeserializer<World>(Sponge.getServer().getWorlds().stream().findAny().get())).create();

        //MapConfig mapConfig = gson.fromJson(networkClient.getMapConfiguration(),
//                    MapConfig.class);

        //getServer().getPluginManager().registerEvents(new LobbyListener(networkClient, mapConfig), this);
    }
}
