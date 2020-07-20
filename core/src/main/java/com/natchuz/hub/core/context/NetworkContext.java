package com.natchuz.hub.core.context;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.SneakyThrows;
import org.bson.UuidRepresentation;

import java.util.Arrays;
import java.util.List;

import com.natchuz.hub.protocol.arch.Services;
import com.natchuz.hub.protocol.messaging.Protocol;
import com.natchuz.hub.protocol.state.RedisStateDatabase;
import com.natchuz.hub.protocol.state.StateDatabase;
import com.natchuz.hub.core.api.PrivilegedFacade;
import com.natchuz.hub.core.map.LocalMapRepository;
import com.natchuz.hub.core.map.MapRepository;
import com.natchuz.hub.core.modules.*;
import com.natchuz.hub.core.profile.CachedProfileRepo;
import com.natchuz.hub.core.profile.MongoProfileRepo;
import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.core.profile.UserProfile;
import com.natchuz.hub.core.proxy.NetworkProxyBackend;
import com.natchuz.hub.core.proxy.ProxyBackend;

/**
 * Represents context of running in full network
 */
public class NetworkContext implements ServerContext {

    private final PrivilegedFacade facade;
    private final MongoDatabase database;
    private final Protocol protocol;

    private final String DB_NAME = "natchuz-hub";

    @SneakyThrows
    public NetworkContext(PrivilegedFacade facade) {
        this.facade = facade;

        MongoClientSettings settings = MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.STANDARD)
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("mongo", 27017)))).build();
        database = MongoClients.create(settings).getDatabase(DB_NAME);

        protocol = new Protocol(Services.PAPER.createClient());
    }

    @Override
    public MapRepository createMapRepository() {
        return new LocalMapRepository(facade.getPlugin().getDataFolder());
    }

    @Override
    public ProxyBackend createProxyBackend() {
        return new NetworkProxyBackend(protocol);
    }

    @Override
    public StateDatabase createStateDatabase() {
        return new RedisStateDatabase("redis");
    }

    @Override
    public <T extends UserProfile> ProfileRepo<T> createProfileRepo(Class<T> tClass) {
        return new CachedProfileRepo<>(new MongoProfileRepo<>(database, tClass));
    }

    @Override
    public List<Module> createModules() {
        return Arrays.asList(
                new FriendsNotifier(facade, protocol),
                new PlayerSubscriber(protocol),
                new NetworkListener(facade),
                new ProtocolHandler(facade, protocol)
        );
    }
}
