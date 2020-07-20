package com.natchuz.hub.utils.mojang;

import java.util.UUID;

public interface MojangAPI {

    /**
     * Returns username of user with specified UUID
     */
    String getUsername(UUID uuid);

    /**
     * Returns UUID of user with specified username
     */
    UUID getUUID(String username);

}
