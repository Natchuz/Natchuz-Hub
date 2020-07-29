package com.natchuz.hub.core.api;

import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

import com.natchuz.hub.core.map.MapManifest;
import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.core.profile.UserProfile;

/**
 * Ships all utils for all gamemodes
 */
public interface MainFacade {

    /**
     * Returns current's {@link MapManifest map manifest}
     */
    MapManifest getMapManifest();

    /**
     * Returns current's map configuration in raw {@link String} to parse it by yourself
     */
    String getMapConfiguration();

    /**
     * Returns UUID of current's map {@link org.spongepowered.api.world.World world}
     */
    UUID getMapWorld();

    /**
     * Sends player to lobby
     */
    void sendToLobby(Player player);

    /**
     * @deprecated until new api will be developed
     */
    <T extends UserProfile> ProfileRepo<T> createProfileRepo(Class<T> clazz);

    /**
     * Server id
     */
    String getServerId();
}
