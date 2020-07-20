package com.natchuz.hub.core.api;

import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.natchuz.hub.protocol.state.StateDatabase;
import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.core.proxy.ProxyBackend;
import com.natchuz.hub.core.user.FriendUtils;
import com.natchuz.hub.core.user.User;

/**
 * Ships all utilities for use inside core library and privileged servers like lobby
 */
public interface PrivilegedFacade extends CoreFacade, StateDatabase, ProxyBackend, ProfileRepo<User> {

    /**
     * Server id
     */
    String getServerId();

    /**
     * @deprecated until new api will be developed
     */
    FriendUtils getFriendUtils();

    /**
     * @deprecated until new api will be developed
     */
    JavaPlugin getPlugin();

    /**
     * @deprecated until new api will be developed
     */
    ProtocolManager getProtocolManager();
}
