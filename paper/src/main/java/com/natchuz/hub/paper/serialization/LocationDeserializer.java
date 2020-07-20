package com.natchuz.hub.paper.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link com.google.gson.Gson GSON} deserializer used to deserialize {@link Location location}
 */
public class LocationDeserializer implements JsonDeserializer<Location> {

    private final Pattern STRING_PATTERN = Pattern.compile("(?<x>-?\\d+(\\.\\d+)?) *: *(?<y>-?\\d+(\\.\\d+)?) *"
            + " *: *(?<z>-?\\d+(\\.\\d+)?)( *: *(?<yaw>-?\\d+(\\.\\d+)?) *: *(?<pitch>-?\\d+(\\.\\d+)?))?");

    private final World world;

    /**
     * @param world world in which all locations should be created
     */
    public LocationDeserializer(World world) {
        this.world = world;
    }

    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            String targetString = json.getAsString();
            double x, y, z;
            float yaw = 0f, pitch = 0f;

            Matcher matcher = STRING_PATTERN.matcher(targetString);

            if (matcher.find()) {
                x = Double.parseDouble(matcher.group("x"));
                y = Double.parseDouble(matcher.group("y"));
                z = Double.parseDouble(matcher.group("z"));
                if (matcher.group("yaw") != null && matcher.group("pitch") != null) {
                    yaw = (float) Double.parseDouble(matcher.group("yaw"));
                    pitch = (float) Double.parseDouble(matcher.group("pitch"));
                }
                return new Location(world, x, y, z, yaw, pitch);
            }
            throw new JsonParseException("Could not deserialize location from string (" + targetString + ") " +
                    "due to bad formatting!");
        }
        throw new JsonParseException("Could not deserialize location from: " + json.toString());
    }
}
