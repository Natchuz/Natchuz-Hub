package com.natchuz.hub.paper.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import com.natchuz.hub.paper.regions.Region;
import com.natchuz.hub.paper.regions.RegionParser;
import com.natchuz.hub.paper.regions.Union;

/**
 * Use this class to deserialize regions.
 */
public class RegionDeserializer implements JsonDeserializer<Region> {

    private final boolean forceUnion;

    /**
     * @param forceUnion whenever deserializer should force using {@link Union} as target object.
     *                   See: {@link RegionParser#parse(String, boolean)}
     */
    public RegionDeserializer(boolean forceUnion) {
        this.forceUnion = forceUnion;
    }

    /**
     * New deserializer with disabled union force
     *
     * @see RegionDeserializer#RegionDeserializer(boolean)
     */
    public RegionDeserializer() {
        this(false);
    }

    @Override
    public Region deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            return RegionParser.parse(json.getAsString(), forceUnion);
        }
        throw new JsonParseException("Could not deserialize region! Invalid json type!");
    }
}
