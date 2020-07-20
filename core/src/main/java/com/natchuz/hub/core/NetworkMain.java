package com.natchuz.hub.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.util.List;

import com.natchuz.hub.paper.managers.DialogManager;
import com.natchuz.hub.paper.managers.HologramManager;
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
@Plugin(name = "NatchuzHub", version = "1.0")
@Author("Natchuz")
@Dependency("ProtocolLib")
@ApiVersion(ApiVersion.Target.v1_13)
@Commands({
        @Command(name = "menu"),
        @Command(name = "hub", aliases = {"l", "h", "lobby"}),
        @Command(name = "friends", aliases = "f"),
        @Command(name = "v")
})
public class NetworkMain extends JavaPlugin implements PrivilegedFacade {
    private ProtocolManager protocolManager;
    private HologramManager hologramManager;
    private SidebarManager sidebarManager;
    private DialogManager dialogManager;
    private FriendUtils friendUtils;
    private String serverID;

    @Delegate
    private ProxyBackend networkUtils;
    @Delegate
    private ProfileRepo<User> userProfileProvider;
    @Delegate
    private StateDatabase stateDatabase;

    private LoadedMap loadedMap;
    private ServerContext context;

    @SneakyThrows
    @Override
    public void onLoad() {
        client = this;

        ContextLoader loader = new PropertiesContextLoader(this);
        context = loader.loadContext();
        getLogger().info("Using context: " + context.getClass().getCanonicalName());

        protocolManager = ProtocolLibrary.getProtocolManager();
        serverID = System.getenv("SERVERID");
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        userProfileProvider = context.createProfileRepo(User.class);
        stateDatabase = context.createStateDatabase();
        networkUtils = context.createProxyBackend();

        MapRepository repository = context.createMapRepository();
        MapLoader loader = new AnvilZipMapLoader(repository);
        loadedMap = loader.load("testMap");

        hologramManager = new HologramManager(this);
        sidebarManager = new SidebarManager("Natchuz Hub", getServerId());
        dialogManager = new DialogManager();
        getServer().getPluginManager().registerEvents(hologramManager, this);
        getServer().getPluginManager().registerEvents(dialogManager, this);

        friendUtils = new FriendUtils();
        CosmeticsListener cosmeticsListener = new CosmeticsListener();
        NetworkCommands networkCommands = new NetworkCommands();

        getServer().getPluginManager().registerEvents(cosmeticsListener, this);
        getServer().getPluginManager().registerEvents(friendUtils, this);
        getServer().getPluginManager().registerEvents(networkCommands, this);

        List<Module> modules = context.createModules();
        modules.forEach(module -> getServer().getPluginManager().registerEvents(module, this));

        stateDatabase.registerServer(ServerID.fromString(System.getenv("SERVERTYPE") + "/" + getServerId())).get();
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        unregisterServer(ServerID.fromString(System.getenv("SERVERTYPE") + "/" + serverID)).get();
    }

    //region facade implementation

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }

    @Override
    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @Override
    public FriendUtils getFriendUtils() {
        return friendUtils;
    }

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
    //region singleton

    private static PrivilegedFacade client;

    /**
     * @deprecated Singletons bad
     */
    public static PrivilegedFacade getInstance() {
        return client;
    }

    //endregion
}
