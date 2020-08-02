package com.natchuz.hub.paper.holograms;

import eu.crushedpixel.sponge.packetgate.api.event.PacketEvent;
import eu.crushedpixel.sponge.packetgate.api.listener.PacketListenerAdapter;
import eu.crushedpixel.sponge.packetgate.api.registry.PacketConnection;
import eu.crushedpixel.sponge.packetgate.api.registry.PacketGate;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import org.javatuples.Pair;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.function.Function;

public class HologramsServiceImpl extends PacketListenerAdapter implements HologramsService {

    private final float LINE_SPACING = 0.27f;

    private final Map<String, Pair<List<UUID>, Function<Player, String[]>>> holograms;
    private final Map<UUID, Pair<String, Integer>> entities;

    public HologramsServiceImpl() {
        holograms = new HashMap<>();
        entities = new HashMap<>();
    }

    @Override
    public void add(String id, Location<World> location, int length, Function<Player, String[]> fun) {
        holograms.put(id, new Pair(new LinkedList<>(), fun));

        for (int i = 0; i < length; i++) {
            Entity entity = location.getExtent().createEntity(EntityTypes.ARMOR_STAND,
                    location.getPosition().add(0, i * LINE_SPACING, 0));

            entity.offer(Keys.INVISIBLE, true);
            entity.offer(Keys.HAS_GRAVITY, false);
            entity.offer(Keys.AI_ENABLED, false);
            entity.offer(Keys.ARMOR_STAND_HAS_BASE_PLATE, false);
            entity.offer(Keys.ARMOR_STAND_MARKER, true);
            entity.offer(Keys.CUSTOM_NAME_VISIBLE, true);
            entity.offer(Keys.DISPLAY_NAME, Text.of("Oh yeah it's all coming together"));

            entities.put(entity.getUniqueId(), new Pair<>(id, length - i - 1));
            holograms.get(id).getValue0().add(entity.getUniqueId());
        }
    }

    @Override
    public void remove() {

    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        PacketGate packetGate = Sponge.getServiceManager().provide(PacketGate.class)
                .orElseThrow(() -> new RuntimeException("PacketGate was not provided, although it was supposed to!"));
        packetGate.registerListener(this, ListenerPriority.DEFAULT);
    }

    @Override
    public void onPacketWrite(PacketEvent packetEvent, PacketConnection connection) {
        /*if (packetEvent.getPacket() instanceof SPacketEntityMetadata) {
            SPacketEntityMetadata packet = (SPacketEntityMetadata) packetEvent.getPacket();
            List<EntityDataManager.DataEntry<?>> entries = packet.dataManagerEntries;
            for (int i = 0; i < entries.size(); i++) {
                EntityDataManager.DataEntry<?> entry = entries.get(i);
                if (entry.getKey().equals(net.minecraft.entity.Entity.CUSTOM_NAME)) {
                    ((EntityDataManager.DataEntry<String>) entry).setValue("Teeeest lol");
                    entries.set(i, entry.copy());
                }
            }
        }*/
    }
}
