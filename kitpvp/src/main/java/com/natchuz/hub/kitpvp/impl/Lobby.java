package com.natchuz.hub.kitpvp.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ZombieVillager;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.natchuz.hub.paper.regions.BlockVectors;
import com.natchuz.hub.core.api.CoreFacade;
import com.natchuz.hub.kitpvp.map.MapConfig;
import com.natchuz.hub.kitpvp.map.MapConfig.KitLocation;

import static com.natchuz.hub.paper.Color.*;

@ToString
public class Lobby {

    @Getter
    private LivingEntity hubNpc;
    @Getter
    private int voidHeight;

    private BiMap<String, ArmorStand> kitPlaceholders;
    private Location spawn;

    private Lobby() {
    }

    public Location getSpawn() {
        return BlockVectors.centerFlat(spawn);
    }

    public Optional<ArmorStand> getArmorStand(String id) {
        return Optional.ofNullable(kitPlaceholders.get(id));
    }

    public Optional<String> getIdFromEntity(Entity entity) {
        return kitPlaceholders.entrySet().stream()
                .filter(a -> entity.getUniqueId().equals(a.getValue().getUniqueId()))
                .map(Map.Entry::getKey)
                .findAny();
    }

    public Set<ArmorStand> getArmorStands() {
        return kitPlaceholders.values();
    }

    public static Lobby createLobby(CoreFacade facade, MapConfig config) {
        Lobby lobby = new Lobby();
        lobby.voidHeight = config.getLobbyVoidHeight();
        lobby.spawn = config.getLobbySpawn();
        lobby.kitPlaceholders = HashBiMap.create();

        {
            Location returnNpcLocation = BlockVectors.centerFlat(config.getHubNpcLocation());
            lobby.hubNpc = returnNpcLocation.getWorld().spawn(returnNpcLocation, ZombieVillager.class);
            lobby.hubNpc.setAI(false);

            facade.getHologramManager().add("lobby", returnNpcLocation.clone().add(0, 2, 0), 2,
                    (p) -> new String[]{RED + BOLD + "RETURN TO LOBBY", YELLOW + "Right click"});
        }

        for (KitLocation kitLocation : config.getKitPlaceholders()) {
            ArmorStand armorStand = kitLocation.getLocation().getWorld().spawn(
                    BlockVectors.centerFlat(kitLocation.getLocation()), ArmorStand.class);
            armorStand.setAI(false);
            armorStand.setArms(true);
            lobby.kitPlaceholders.put(kitLocation.getId(), armorStand);

            facade.getHologramManager().add(kitLocation.getId(), BlockVectors.centerFlat(
                    kitLocation.getLocation()).add(0, 2, 0),
                    1, (p) -> new String[]{"Kit name"});
        }

        for (Location loc : config.getKitsClickInformation()) {
            facade.getHologramManager().add(loc.toString(), BlockVectors.centerFlat(loc),
                    3, (p) -> new String[]{YELLOW + BOLD + "LEFT CLICK", YELLOW + "To join arena",
                            GREY + "Right click to select spawn"});
        }

        return lobby;
    }
}
