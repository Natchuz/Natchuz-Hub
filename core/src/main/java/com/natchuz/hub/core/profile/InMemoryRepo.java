package com.natchuz.hub.core.profile;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryRepo<T extends UserProfile> implements ProfileRepo<T> {

    private final HashMap<UUID, T> usersMap;
    private final Class<T> clazz;

    public InMemoryRepo(Class<T> clazz) {
        this.clazz = clazz;
        usersMap = new HashMap<>();
    }

    @SneakyThrows
    @Override
    public T getProfile(UUID uuid) {
        if (usersMap.containsKey(uuid)) {
            return usersMap.get(uuid);
        } else {
            T profile = clazz.newInstance();
            profile.setUUID(uuid);
            profile.initNew();
            usersMap.put(uuid, profile);
            return profile;
        }
    }

    @Override
    public void updateProfile(T profile) {
        usersMap.replace(profile.getUUID(), profile);
    }
}
