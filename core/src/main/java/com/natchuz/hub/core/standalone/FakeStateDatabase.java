package com.natchuz.hub.core.standalone;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.natchuz.hub.protocol.state.*;

/**
 * This is fake state database, that actually returns nothing
 */
public class FakeStateDatabase implements StateDatabase {

    @Override
    public CompletableFuture<Optional<PlayerInfo>> getPlayer(UUID uuid) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

    @Override
    public CompletableFuture<Void> setPlayerLocation(UUID uuid, String serverID) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> setPlayerJoinFlags(UUID uuid, JoinFlags... flags) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Boolean> dropPlayer(UUID uuid) {
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Optional<Server>> getServer(ServerID id) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

    @Override
    public CompletableFuture<List<Server>> getServers(String namespace) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    @Override
    public CompletableFuture<Void> registerServer(ServerID id) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> unregisterServer(ServerID id) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Integer> incrPlayers(ServerID id) {
        return CompletableFuture.completedFuture(0);
    }

    @Override
    public CompletableFuture<Integer> decrPlayers(ServerID id) {
        return CompletableFuture.completedFuture(0);
    }
}
