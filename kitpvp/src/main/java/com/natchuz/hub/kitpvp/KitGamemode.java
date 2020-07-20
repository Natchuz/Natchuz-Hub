package com.natchuz.hub.kitpvp;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.natchuz.hub.core.api.CoreFacade;
import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.kitpvp.impl.PlayerState;
import com.natchuz.hub.kitpvp.model.KitConfig;
import com.natchuz.hub.kitpvp.model.KitProfile;

/**
 * This is fancy interface to pack all core components of kitpvp gamemode
 * <p>
 * Only use when required
 */
public interface KitGamemode extends ProfileRepo<KitProfile>, CoreFacade {

    /**
     * @see com.natchuz.hub.kitpvp.impl.StateHolder#initNewState(Player, PlayerState)
     */
    void initNewState(Player player, PlayerState state);

    /**
     * @see com.natchuz.hub.kitpvp.impl.StateHolder#getState(Player)
     */
    PlayerState getState(Player player);

    Plugin getPlugin();

    KitConfig getKitConfig();
}
