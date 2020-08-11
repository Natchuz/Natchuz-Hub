package com.natchuz.hub.kitpvp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.DependsOn;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.io.InputStreamReader;
import java.util.UUID;

import com.natchuz.hub.sponge.regions.Region;
import com.natchuz.hub.sponge.serialization.ItemStackAdapter;
import com.natchuz.hub.sponge.serialization.LocationDeserializer;
import com.natchuz.hub.sponge.serialization.RegionDeserializer;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.api.MainFacade;
import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.kitpvp.impl.*;
import com.natchuz.hub.kitpvp.map.MapConfig;
import com.natchuz.hub.kitpvp.model.KitConfig;
import com.natchuz.hub.kitpvp.model.KitProfile;

/**
 * Entry class for KitPvP Plugin
 */
@Plugin(name = "KitPvP", version = "1.0")
@Author("Natchuz")
@DependsOn({@Dependency("NatchuzHub"), @Dependency("ProtocolLib")})
@ApiVersion(ApiVersion.Target.v1_13)
public class KitPlugin extends JavaPlugin implements ConcreteKitGamemode {

    @Delegate
    private CoreFacade facade;
    @Delegate
    private StateHolder holder;
    @Getter
    private Lobby mainLobby;
    @Getter
    private Arena mainArena;
    @Getter
    private KitConfig kitConfig;

    private ProfileRepo<KitProfile> provider;

    @Override
    public void onEnable() {
        facade = getPlugin(NetworkMain.class);
        provider = facade.createProfileRepo(KitProfile.class);

        Gson gson = new GsonBuilder().registerTypeAdapter(Region.class, new RegionDeserializer())
                .registerTypeAdapter(Location.class, new LocationDeserializer(facade.getMapWorld()))
                .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
                .create();

        kitConfig = gson.fromJson(new InputStreamReader(getResource("config.json")), KitConfig.class);
        getLogger().info("Loaded gamemode config as " + kitConfig);

        MapConfig mapConfig = gson.fromJson(facade.getMapConfiguration(), MapConfig.class);
        getLogger().info("Loaded map config as " + mapConfig);

        mainLobby = Lobby.createLobby(facade, mapConfig);
        mainArena = Arena.createArena(mapConfig);

        getServer().getPluginManager().registerEvents(new LobbyController(mainLobby), this);

        holder = new StateHolder();

        getServer().getPluginManager().registerEvents(new StateHolderController(holder), this);
        getServer().getPluginManager().registerEvents(new EntryListener(this), this);
        getServer().getPluginManager().registerEvents(WorldController.initWorldController(mapConfig, this,
                facade.getMapWorld()), this);
    }


    @Override
    public KitProfile getProfile(UUID uuid) {
        return provider.getProfile(uuid);
    }

    @Override
    public void updateProfile(KitProfile profile) {
        provider.updateProfile(profile);
    }

    @Override
    public org.bukkit.plugin.Plugin getPlugin() {
        return this;
    }
}
