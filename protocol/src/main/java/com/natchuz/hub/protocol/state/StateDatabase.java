package com.natchuz.hub.protocol.state;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents microservice that provides information about different players and their temporary state
 */
public interface StateDatabase {

    //region players

    /**
     * Gets {@link PlayerInfo player info}
     *
     * @return future of empty optional if not found, or value
     */
    CompletableFuture<Optional<PlayerInfo>> getPlayer(UUID uuid);

    /**
     * Sets server location for specified player
     *
     * @return future
     */
    CompletableFuture<Void> setPlayerLocation(UUID uuid, String serverID);

    /**
     * Sets {@link JoinFlags join flags} for specified player
     *
     * @return future
     */
    CompletableFuture<Void> setPlayerJoinFlags(UUID uuid, JoinFlags... flags);

    /**
     * Removes player from state, meaning they left the server
     *
     * @return future indicating success
     */
    CompletableFuture<Boolean> dropPlayer(UUID uuid);

    //endregion
    //region servers

    CompletableFuture<Optional<Server>> getServer(ServerID id);

    CompletableFuture<List<Server>> getServers(String namespace);

    CompletableFuture<Void> registerServer(ServerID id);

    CompletableFuture<Void> unregisterServer(ServerID id);

    CompletableFuture<Integer> incrPlayers(ServerID id);

    CompletableFuture<Integer> decrPlayers(ServerID id);

    //endregion
}
