package com.natchuz.hub.core.profile;

import java.util.UUID;

/**
 * Keeps user profiles
 *
 * @param <T> profile type
 */
public interface ProfileRepo<T extends UserProfile> {

    /**
     * Get profile
     *
     * @param uuid UUID of profile that corresponds to player UUID
     * @return profile
     */
    T getProfile(UUID uuid);

    /**
     * Update profile
     *
     * @param profile profile to be updated
     */
    void updateProfile(T profile);
}
