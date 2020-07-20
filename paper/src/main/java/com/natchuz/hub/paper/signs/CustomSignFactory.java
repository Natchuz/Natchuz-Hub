package com.natchuz.hub.paper.signs;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

/**
 * Factory of packet adapters for personalized signs
 */
public class CustomSignFactory {

    /**
     * Only creates adapter
     *
     * @param plugin   plugin which register adapter to
     * @param function sign content provider
     * @param location location of sign
     * @return created adapter
     */
    public static PacketListener createAdapter(Plugin plugin, Function<Player, SignContent> function,
                                               Location location) {
        return new CustomAdapter(plugin, function, location);
    }

    /**
     * Creates packet adapter and register it
     *
     * @param manager ProtocolManager which register adapter to
     * @see CustomSignFactory#createAdapter(ProtocolManager, Plugin, Function, Location)
     */
    public static PacketListener createAdapter(ProtocolManager manager, Plugin plugin,
                                               Function<Player, SignContent> function, Location location) {
        PacketListener adapter = createAdapter(plugin, function, location);
        manager.addPacketListener(adapter);
        return adapter;
    }

    private static class CustomAdapter extends PacketAdapter {

        private final Location location;
        private final Function<Player, SignContent> function;

        public CustomAdapter(Plugin plugin, Function<Player, SignContent> function, Location location) {
            super(plugin,
                    PacketType.Play.Server.TILE_ENTITY_DATA,
                    PacketType.Play.Server.MAP_CHUNK);
            this.function = function;
            this.location = location;
        }

        @Override
        public void onPacketSending(PacketEvent event) {
            PacketContainer packet = event.getPacket();

            if (packet.getType() == PacketType.Play.Server.TILE_ENTITY_DATA) {

                Location location = packet.getBlockPositionModifier().read(0)
                        .toLocation(event.getPlayer().getWorld());

                if (this.location.equals(location)) {
                    String[] result = function.apply(event.getPlayer()).getArray();
                    NbtCompound compound = (NbtCompound) packet.getNbtModifier().read(0);

                    for (int i = 0; i < 4; i++) {
                        compound.put("Text" + (i + 1), WrappedChatComponent.fromText(result[i]).getJson());
                    }

                    packet.getNbtModifier().write(0, compound);
                }
            }
            if (packet.getType() == PacketType.Play.Server.MAP_CHUNK) {
                event.getPlayer().sendSignChange(location, function.apply(event.getPlayer()).getArray());
            }
        }
    }

}
