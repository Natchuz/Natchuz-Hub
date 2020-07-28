package com.natchuz.hub.core.api;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.natchuz.hub.paper.managers.DialogManager;
import com.natchuz.hub.paper.managers.SidebarManager;
import com.natchuz.hub.core.map.MapManifest;
import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.core.profile.UserProfile;

/**
 * Ships all utils for all gamemodes
 */
public interface CoreFacade {

    /**
     * Returns current's {@link MapManifest map manifest}
     */
    MapManifest getMapManifest();

    /**
     * Returns current's map configuration in raw {@link String} to parse it by yourself
     */
    String getMapConfiguration();

    /**
     * Returns current's map {@link World world}
     */
    World getMapWorld();

    /**
     * Sends player to lobby
     */
    void sendToLobby(Player player);

    /**
     * @deprecated until new api will be developed
     */
    HologramManager getHologramManager();

    /**
     * @deprecated until new api will be developed
     */
    SidebarManager getSidebarManager();

    /**
     * @deprecated until new api will be developed
     */
    DialogManager getDialogManager();

    /**
     * @deprecated until new api will be developed
     */
    <T extends UserProfile> ProfileRepo<T> createProfileRepo(Class<T> clazz);
}
