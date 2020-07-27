package com.natchuz.hub.paper.holograms;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;

public interface HologramsService {

    void add(String id, Location<?> location, List<Text> lines);

    void remove();

}
