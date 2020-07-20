package com.natchuz.hub.core.user;

import org.bukkit.entity.Player;

import java.util.UUID;

import com.natchuz.hub.core.NetworkMain;

public interface Purchasable {
    Integer getItemId();

    default boolean ownedBy(UUID uuid) {
        return NetworkMain.getInstance().getProfile(uuid).owns(this);
    }

    default boolean ownedBy(Player player) {
        return ownedBy(player.getUniqueId());
    }
}
