package com.natchuz.hub.core.context;

import java.util.Collections;
import java.util.List;

import com.natchuz.hub.protocol.state.StateDatabase;
import com.natchuz.hub.core.api.PrivilegedFacade;
import com.natchuz.hub.core.map.LocalMapRepository;
import com.natchuz.hub.core.map.MapRepository;
import com.natchuz.hub.core.modules.Module;
import com.natchuz.hub.core.profile.InMemoryRepo;
import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.core.profile.UserProfile;
import com.natchuz.hub.core.proxy.ProxyBackend;
import com.natchuz.hub.core.proxy.StandaloneProxyBackend;
import com.natchuz.hub.core.standalone.FakeStateDatabase;

/**
 * Standalone context is used for testing
 */
public class StandaloneContext implements ServerContext {

    private final PrivilegedFacade facade;

    public StandaloneContext(PrivilegedFacade facade) {
        this.facade = facade;
    }

    @Override
    public MapRepository createMapRepository() {
        return new LocalMapRepository(facade.getPlugin().getDataFolder());
    }

    @Override
    public ProxyBackend createProxyBackend() {
        return new StandaloneProxyBackend();
    }

    @Override
    public StateDatabase createStateDatabase() {
        return new FakeStateDatabase();
    }

    @Override
    public <T extends UserProfile> ProfileRepo<T> createProfileRepo(Class<T> tClass) {
        return new InMemoryRepo<>(tClass);
    }

    @Override
    public List<Module> createModules() {
        return Collections.emptyList();
    }
}
