package com.natchuz.hub.core;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.List;

import com.natchuz.hub.paper.managers.DialogManager;
import com.natchuz.hub.paper.managers.SidebarManager;
import com.natchuz.hub.protocol.state.JoinFlags;
import com.natchuz.hub.protocol.state.ServerID;
import com.natchuz.hub.protocol.state.StateDatabase;
import com.natchuz.hub.core.api.PrivilegedFacade;
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
 * This is entry class of Network plugin for Paper
 */
@Plugin(id = "core-plugin", name = "Core Plugin", version = "1.0")
public class NetworkMain implements PrivilegedFacade {

    private HologramManager hologramManager;
    private SidebarManager sidebarManager;
    private DialogManager dialogManager;
    private FriendUtils friendUtils;
    private String serverID;

    private ProxyBackend networkUtils;
    private ProfileRepo<User> userProfileProvider;
    private StateDatabase stateDatabase;

    private LoadedMap loadedMap;
    private ServerContext context;

    @Inject
    private Game game;

    @Inject
    private Logger logger;

    @Listener
    public void onServerLoad(GamePreInitializationEvent event) {

        // load context
        ContextLoader loader = new PropertiesContextLoader(this);
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
        stateDatabase = context.createStateDatabase();
        networkUtils = context.createProxyBackend();

        // load test map
        MapRepository repository = context.createMapRepository();
        MapLoader loader = new AnvilZipMapLoader(repository);
        loadedMap = loader.load("testMap");

        // TODO: get rid of it to external plugin
        // construct and register all paper utils
        hologramManager = new HologramManager((org.bukkit.plugin.Plugin) this);
        sidebarManager = new SidebarManager("Natchuz Hub", getServerId());
        dialogManager = new DialogManager();
        game.getEventManager().registerListeners(this, hologramManager);
        game.getEventManager().registerListeners(this, dialogManager);

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
        modules.forEach(module -> game.getEventManager().registerListeners(module, this));

        // register server in state database
        stateDatabase.registerServer(ServerID.fromString(System.getenv("SERVERTYPE") + "/" + getServerId())).get();
    }

    @Listener
    public void onPost(GamePostInitializationEvent event) {
        // register all services
        game.getServiceManager().setProvider(this, StateDatabase.class, stateDatabase);
        game.getServiceManager().setProvider(this, ProxyBackend.class, networkUtils);
        game.getServiceManager().setProvider(this, FriendUtils.class, friendUtils);
    }

    @Listener
    public void onStop(GameStoppedEvent event) {
        // unregister server from state database
        stateDatabase.unregisterServer(ServerID.fromString(System.getenv("SERVERTYPE") + "/" + serverID)).get();
    }

    //region facade implementation

    @Override
    public HologramManager getHologramManager() {
        return hologramManager;
    }

    @Override
    public MapManifest getMapManifest() {
        return loadedMap.getManifest();
    }

    @Override
    public String getMapConfiguration() {
        return loadedMap.getMapConfiguration();
    }

    @Override
    public World getMapWorld() {
        return loadedMap.getWorld();
    }

    @Override
    public void sendToLobby(Player player) {
        networkUtils.send(player, "lb", JoinFlags.LOBBY_RETURN);
    }

    @Override
    public SidebarManager getSidebarManager() {
        return sidebarManager;
    }

    @Override
    public DialogManager getDialogManager() {
        return dialogManager;
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
