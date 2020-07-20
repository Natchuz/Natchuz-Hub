package com.natchuz.hub.kitpvp;

import com.natchuz.hub.kitpvp.impl.Arena;
import com.natchuz.hub.kitpvp.impl.Lobby;

/**
 * This interface adds all static-like dependencies in current gamemode
 * (like lobby and arena, if in current implementation there are only 2 of them), to avoid constructor madness
 * <p>
 * Only use when required
 */
public interface ConcreteKitGamemode extends KitGamemode {
    Lobby getMainLobby();

    Arena getMainArena();
}
