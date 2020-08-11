package com.natchuz.hub.core.context;

import org.spongepowered.api.Sponge;
import sun.reflect.generics.factory.CoreReflectionFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.natchuz.hub.protocol.state.StateDatabase;
import com.natchuz.hub.core.api.MainFacade;
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

    private final MainFacade facade;

    public StandaloneContext(MainFacade facade) {
        this.facade = facade;
    }

    @Override
    public MapRepository createMapRepository() {
        File repo = new File(Sponge.getGame().getGameDirectory().toFile().getAbsoluteFile() + "/maps");
        repo.mkdirs();
        return new LocalMapRepository(repo);
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
