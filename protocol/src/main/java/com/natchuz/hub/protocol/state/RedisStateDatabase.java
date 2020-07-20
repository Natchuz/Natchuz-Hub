package com.natchuz.hub.protocol.state;

import lombok.Cleanup;
import redis.clients.jedis.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.natchuz.hub.utils.UUIDConverter;

/**
 * An implementation of {@link StateDatabase state database} on redis
 */
public class RedisStateDatabase implements StateDatabase {

    private final JedisPool pool;
    private final ExecutorService executor;

    public RedisStateDatabase(String address) {
        this.pool = new JedisPool(new JedisPoolConfig(), address);
        this.executor = Executors.newCachedThreadPool();
    }

    private String getPlayerHash(UUID uuid) {
        return "players." + UUIDConverter.toCondensed(uuid);
    }

    //region players

    @Override
    public CompletableFuture<Optional<PlayerInfo>> getPlayer(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            @Cleanup Jedis jedis = pool.getResource();
            Transaction transaction = jedis.multi();

            String playerHash = getPlayerHash(uuid);

            Response<Boolean> exist = transaction.exists(playerHash);
            Response<String> location = transaction.hget(playerHash, "location");
            Response<String> flags = transaction.hget(playerHash, "flags");
            transaction.exec();

            if (!exist.get()) {
                return Optional.empty();
            }

            EnumSet<JoinFlags> parsedFlags = Arrays.stream(flags.get().split(":"))
                    .map(JoinFlags::valueOf)
                    .collect(Collectors.toCollection(() -> EnumSet.noneOf(JoinFlags.class)));

            return Optional.of(new PlayerInfo(uuid, location.get(), parsedFlags));
        }, executor);
    }

    @Override
    public CompletableFuture<Void> setPlayerLocation(UUID uuid, String serverID) {
        return CompletableFuture.runAsync(() -> {
            @Cleanup Jedis jedis = pool.getResource();
            jedis.hset(getPlayerHash(uuid), "location", serverID);
        }, executor);
    }

    @Override
    public CompletableFuture<Void> setPlayerJoinFlags(UUID uuid, JoinFlags... flags) {
        return CompletableFuture.runAsync(() -> {
            @Cleanup Jedis jedis = pool.getResource();
            jedis.hset(getPlayerHash(uuid), "flags",
                    Arrays.stream(flags)
                            .map(JoinFlags::toString)
                            .collect(Collectors.joining(":")));
        }, executor);
    }

    @Override
    public CompletableFuture<Boolean> dropPlayer(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = pool.getResource()) {
                return jedis.del(getPlayerHash(uuid)) > 0;
            }
        }, executor);
    }

    //endregion
    //region server

    @Override
    public CompletableFuture<Optional<Server>> getServer(ServerID id) {
        return CompletableFuture.supplyAsync(() -> {
            @Cleanup Jedis jedis = pool.getResource();

            String redisKey = id.getFullNamespace().replace('/', '.');
            Transaction transaction = jedis.multi();
            Response<Double> players = transaction.zscore(redisKey, id.getId());

            if (players.get() == null) {
                return Optional.empty();
            }

            return Optional.of(new Server(id, players.get().intValue()));
        }, executor);
    }

    @Override
    public CompletableFuture<List<Server>> getServers(String namespace) {
        return CompletableFuture.supplyAsync(() -> {
            @Cleanup Jedis jedis = pool.getResource();
            Set<Tuple> result = jedis.zrangeWithScores(namespace.replace('/', '.'), 0, -1);
            return result.stream().map(t -> new Server(ServerID.fromString(namespace + "/" + t.getElement()),
                    (int) t.getScore())).collect(Collectors.toList());
        });
    }

    @Override
    public CompletableFuture<Void> registerServer(ServerID id) {
        return CompletableFuture.runAsync(() -> {
            @Cleanup Jedis jedis = pool.getResource();
            jedis.zadd(id.getFullNamespace().replace('/', '.'), 0, id.getId());
        });
    }

    @Override
    public CompletableFuture<Void> unregisterServer(ServerID id) {
        return CompletableFuture.runAsync(() -> {
            @Cleanup Jedis jedis = pool.getResource();
            jedis.zrem(id.getFullNamespace().replace('/', '.'), id.getId());
        });
    }

    @Override
    public CompletableFuture<Integer> incrPlayers(ServerID id) {
        return CompletableFuture.supplyAsync(() -> {
            @Cleanup Jedis jedis = pool.getResource();
            String redisKey = id.getFullNamespace().replace('/', '.');
            return jedis.zincrby(redisKey, 1, id.getId()).intValue();
        });
    }

    @Override
    public CompletableFuture<Integer> decrPlayers(ServerID id) {
        return CompletableFuture.supplyAsync(() -> {
            @Cleanup Jedis jedis = pool.getResource();
            String redisKey = id.getFullNamespace().replace('/', '.');
            return jedis.zincrby(redisKey, -1, id.getId()).intValue();
        });
    }

    //endregion
}
