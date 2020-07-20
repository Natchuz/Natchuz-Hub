package com.natchuz.hub.paper.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackAdapter implements JsonDeserializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            String value = json.getAsString();
            String[] parts = value.split(":");

            Material material = Material.getMaterial(parts[0]);
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
        }
        return null;
    }
}
