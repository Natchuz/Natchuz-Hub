package com.natchuz.hub.core;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.util.List;
import java.util.UUID;

import com.natchuz.hub.core.api.MainFacade;
import com.natchuz.hub.core.content.commands.NetworkCommands;
import com.natchuz.hub.core.content.cosmetics.CosmeticsListener;
import com.natchuz.hub.core.context.ContextLoader;
import com.natchuz.hub.core.context.PropertiesContextLoader;
import com.natchuz.hub.core.context.ServerContext;
import com.natchuz.hub.core.map.*;
import com.natchuz.hub.core.modules.Module;
import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.core.profile.UserProfile;
import com.natchuz.hub.core.proxy.ProxyBackend;
import com.natchuz.hub.core.user.FriendUtils;
import com.natchuz.hub.core.user.User;

/**
 * This is entry class of Core plugin for Sponge
 */
@Plugin(id = CorePlugin.ID, name = "Core Plugin", version = "1.0",
        dependencies = @Dependency(id = "natchuz-hub-sponge-utils"))
public class CorePlugin implements MainFacade {

    public static final String ID = "natchuz-hub-core";

    private FriendUtils friendUtils;
    private String serverID;

    private ProxyBackend networkUtils;
    private ProfileRepo<User> userProfileProvider;

    private LoadedMap loadedMap;
    private ServerContext context;

    @Inject
    private Game game;

    @Inject
    private Logger logger;

    @Listener
    public void onServerLoad(GamePreInitializationEvent event) {

        // load context
        ContextLoader loader = new PropertiesContextLoader();
        context = loader.loadContext();
        logger.info("Using context: " + context.getClass().getCanonicalName());

        // obtain server ID
        serverID = System.getenv("SERVERID");
    }

    @SneakyThrows
    @Listener
    public void onEnable(GameInitializationEvent event) {

        // construct basic services
        userProfileProvider = context.createProfileRepo(User.class);
        networkUtils = context.createProxyBackend();

        // construct fixed listeners
        friendUtils = new FriendUtils();
        CosmeticsListener cosmeticsListener = new CosmeticsListener();
        NetworkCommands networkCommands = new NetworkCommands();

        // load fixed listeners
        game.getEventManager().registerListeners(this, cosmeticsListener);
        game.getEventManager().registerListeners(this, friendUtils);
        game.getEventManager().registerListeners(this, networkCommands);

        // load all context-depended listeners
        List<Module> modules = context.createModules();
        modules.forEach(module -> game.getEventManager().registerListeners(this, module));

        // TODO: register
        // register server in state database
        //stateDatabase.registerServer(ServerID.fromString(System.getenv("SERVERTYPE") + "/" + getServerId())).get();
    }

    @SneakyThrows
    @Listener
    public void onStarting(GameStartingServerEvent event) {
        // load test map
        MapRepository repository = context.createMapRepository();
        MapLoader loader = new AnvilZipMapLoader(repository);
        loadedMap = loader.load("testMap");
    }

    @Listener
    public void onPost(GamePostInitializationEvent event) {
        // register all services
        game.getServiceManager().setProvider(this, ProxyBackend.class, networkUtils);
        game.getServiceManager().setProvider(this, FriendUtils.class, friendUtils);
    }

    @Listener
    public void onStop(GameStoppedEvent event) {
        // TODO
        // unregister server from state database
        //stateDatabase.unregisterServer(ServerID.fromString(System.getenv("SERVERTYPE") + "/" + serverID)).get();
    }

    //region facade implementation

    @Override
    public MapManifest getMapManifest() {
        return loadedMap.getManifest();
    }

    @Override
    public String getMapConfiguration() {
        return loadedMap.getMapConfiguration();
    }

    @Override
    public UUID getMapWorld() {
        return loadedMap.getWorld();
    }

    @Override
    public String getServerId() {
        return serverID;
    }

    @Override
    public <T extends UserProfile> ProfileRepo<T> createProfileRepo(Class<T> clazz) {
        return context.createProfileRepo(clazz);
    }

    //endregion
}
