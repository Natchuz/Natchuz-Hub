package com.natchuz.hub.lobby;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.natchuz.hub.paper.serialization.LocationDeserializer;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.api.PrivilegedFacade;

public class HubPlugin extends JavaPlugin {

    private PrivilegedFacade networkClient;

    @Override
    public void onEnable() {
        networkClient = getPlugin(NetworkMain.class);

        Gson gson = new GsonBuilder().registerTypeAdapter(Location.class,
                new LocationDeserializer(networkClient.getMapWorld())).create();

        MapConfig mapConfig = gson.fromJson(networkClient.getMapConfiguration(),
                MapConfig.class);

        getServer().getPluginManager().registerEvents(new LobbyListener(networkClient, mapConfig), this);
    }
}
