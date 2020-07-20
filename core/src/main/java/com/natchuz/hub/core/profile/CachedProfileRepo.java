package com.natchuz.hub.core.profile;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;

import java.util.UUID;

/**
 * A repo that cache profiles in memory
 *
 * @param <T> user profile class
 */
public class CachedProfileRepo<T extends UserProfile> implements ProfileRepo<T> {

    private final ProfileRepo<T> repo;
    private final LoadingCache<UUID, T> cache;

    /**
     * @param repo upstream repo to pull from
     */
    public CachedProfileRepo(ProfileRepo<T> repo) {
        this.repo = repo;
        cache = CacheBuilder.newBuilder().maximumSize(40).build(new CacheLoader<UUID, T>() {
            @Override
            public T load(UUID key) throws Exception {
                return repo.getProfile(key);
            }
        });
    }

    @SneakyThrows
    @Override
    public T getProfile(UUID uuid) {
        return cache.get(uuid);
    }

    @Override
    public void updateProfile(T profile) {
        repo.updateProfile(profile);
        cache.put(profile.getUUID(), profile);
    }
}
