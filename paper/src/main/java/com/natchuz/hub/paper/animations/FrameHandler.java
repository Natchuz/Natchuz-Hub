package com.natchuz.hub.paper.animations;

import org.bukkit.entity.Player;

public interface FrameHandler<V> {
    void handle(V handle, int tick, Player player);
}
