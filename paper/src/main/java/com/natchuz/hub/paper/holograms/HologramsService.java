package com.natchuz.hub.paper.holograms;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.function.Function;

public interface HologramsService {

    void add(String id, Location<World> location, int length, Function<Player, String[]> fun);

    void remove();

}
