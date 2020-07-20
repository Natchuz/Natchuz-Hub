package com.natchuz.hub.paper.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.javatuples.Pair;

import java.util.*;
import java.util.function.Function;

/**
 * Manages holograms
 * <p>
 * Key idea is it will create Armor Stands that will have random tags, and then capture and modify outgoing packets
 */
public class HologramManager implements Listener {

    private final Map<String, Pair<List<ArmorStand>, Function<Player, String[]>>> holograms;
    private final Map<UUID, Pair<String, Integer>> entities;
    private final Random random;
    private final Plugin plugin;

    private final float LINE_SPACING = 0.27f;

    /**
     * Creates simple HologramManager
     */
    public HologramManager(Plugin plugin) {
        this.plugin = plugin;
        holograms = new HashMap<>();
        entities = new HashMap<>();
        random = new Random();
    }

    /**
     * Creates a hologram at certain location and id
     *
     * @param id     id of the hologram
     * @param loc    location of the hologram
     * @param length how many lines hologram will have
     * @param fun    hologram constructor
     */
    public void add(String id, Location loc, int length, Function<Player, String[]> fun) {
        holograms.put(id, new Pair(new LinkedList<>(), fun));

        for (int i = 0; i < length; i++) {
            ArmorStand a = loc.getWorld().spawn(
                    new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ())
                            .add(0, i * LINE_SPACING, 0),
                    ArmorStand.class);

            a.setCustomName("Oh yeah it's all coming together");

            a.setCustomNameVisible(true);
            a.setGravity(false);
            a.setVisible(false);
            a.setAI(false);
            a.setMarker(true);
            a.setBasePlate(false);

            entities.put(a.getUniqueId(), new Pair<>(id, length - i - 1));
            holograms.get(id).getValue0().add(a);
        }
    }

    /**
     * Remove a hologram
     *
     * @param id id of the hologram to remove
     */
    public void remove(String id) {
        for (ArmorStand a : holograms.get(id).getValue0()) {
            entities.remove(a.getUniqueId());
            a.remove();
        }
        holograms.remove(id);
    }

    /**
     * Refresh hologram
     *
     * @param id id of the hologram to refresh
     */
    public void refresh(String id) {
        for (ArmorStand a : holograms.get(id).getValue0()) {
            a.setCustomName(String.valueOf(random.nextInt()));
        }
    }

    //region event handlers

    @EventHandler
    private void onEnable(PluginEnableEvent event) {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, PacketType.Play.Server.ENTITY_METADATA) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {
                            try {
                                PacketContainer metadataPacket = packet.deepClone();
                                List<WrappedWatchableObject> dataWatcherValues = metadataPacket.getWatchableCollectionModifier().read(0);

                                //and this is this the part of code, when no one have clue what's going on and why
                                for (WrappedWatchableObject watchableObject : dataWatcherValues) {
                                    if (watchableObject.getIndex() == 2) {
                                        UUID uuid = metadataPacket.getEntityModifier(event.getPlayer().getWorld()).read(0).getUniqueId();
                                        String obj = holograms.get(entities.get(uuid).getValue0())
                                                .getValue1().apply(event.getPlayer())[entities.get(uuid).getValue1()];
                                        watchableObject.setValue(Optional.of(WrappedChatComponent.fromText(obj).getHandle()));
                                    }
                                }
                                event.setPacket(metadataPacket);
                            } catch (NullPointerException ignored) {
                            }
                        }
                    }
                });
    }

    @EventHandler
    private void onDisable(PluginDisableEvent event) {
        for (Pair<List<ArmorStand>, Function<Player, String[]>> l : holograms.values()) {
            for (ArmorStand a : l.getValue0()) {
                a.remove();
            }
        }
    }

    //endregion
}
