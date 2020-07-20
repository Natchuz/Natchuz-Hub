package com.natchuz.hub.core.context;

import java.util.List;

import com.natchuz.hub.protocol.state.StateDatabase;
import com.natchuz.hub.core.map.MapRepository;
import com.natchuz.hub.core.modules.Module;
import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.core.profile.UserProfile;
import com.natchuz.hub.core.proxy.ProxyBackend;

/**
 * ServerContext is an abstract factory for a different backends, starting from simple Bungeecord client to map loading etc.
 * This will be used in future to create testing context that will fake all backends, to make gamemode testing easier.
 */
public interface ServerContext {

    /**
     * Creates {@link MapRepository map repository} for this context
     */
    MapRepository createMapRepository();

    /**
     * Creates a {@link ProxyBackend proxy backaend}
     */
    ProxyBackend createProxyBackend();

    /**
     * Create a {@link StateDatabase state database}
     */
    StateDatabase createStateDatabase();

    /**
     * Create a {@link ProfileRepo profile repo} to load users from
     *
     * @param <T> type of repo
     */
    <T extends UserProfile> ProfileRepo<T> createProfileRepo(Class<T> tClass);

    /**
     * Creates a list of {@link Module modules} required to be registered in this network context
     */
    List<Module> createModules();

}
