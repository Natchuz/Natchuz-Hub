package com.natchuz.hub.kitpvp.model;

import lombok.Value;

import java.util.List;
import java.util.Optional;

import com.natchuz.hub.kitpvp.model.content.Kit;

@Value
public class KitConfig {
    List<Kit> defaultKits;

    public Optional<Kit> getKit(String id) {
        return defaultKits.stream().filter(k -> k.getName().equals(id)).findAny();
    }
}
