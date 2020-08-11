package com.natchuz.hub.sponge.serialization;

import com.google.gson.*;
import lombok.SneakyThrows;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.lang.reflect.Type;
import java.util.Optional;

public class ItemStackAdapter implements JsonDeserializer<ItemStack> {

    @SneakyThrows
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        /*if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            String value = json.getAsString();
            String[] parts = value.split(":");

            if (material == null) {
                throw new JsonParseException("Invalid Material type: " + value);
            }
            int amount = 1;
            if (parts.length == 2) {
                try {
                    amount = Integer.parseInt(parts[1]);
                } catch (NumberFormatException ignored) {
                    throw new JsonParseException("Amount in material isn't number: " + parts[1]);
                }
            } else if (parts.length != 1) {
                throw new JsonParseException("Wrong format of material with specified amount!");
            }
            return new ItemStack(material, amount);
        }*/
        if (json.isJsonObject()) {
            DataContainer container = DataFormats.JSON.read(json.toString());
            Optional<ItemStackSnapshot> snapshot = Sponge.getDataManager().deserialize(ItemStackSnapshot.class, container);
            return snapshot.map(ItemStackSnapshot::createStack).orElse(null);
        }
        return null;
    }
}
