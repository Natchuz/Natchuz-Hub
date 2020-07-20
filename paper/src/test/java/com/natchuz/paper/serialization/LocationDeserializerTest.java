package com.natchuz.paper.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonPrimitive;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.Test;

import com.natchuz.hub.paper.serialization.LocationDeserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class LocationDeserializerTest {

    @Test
    public void testDeserializer() {
        World targetWorld = mock(World.class, "target-world");

        LocationDeserializer locationDeserializer = new LocationDeserializer(targetWorld);

        assertEquals(new Location(targetWorld, 1, 5, -1.1),
                locationDeserializer.deserialize(new JsonPrimitive("1:5 :-1.1"), Location.class,
                        mock(JsonDeserializationContext.class)));
        assertEquals(new Location(targetWorld, 1, 5, -1.1, 4, 2.1f),
                locationDeserializer.deserialize(new JsonPrimitive("1:5: -1.1:  4: 2.1"), Location.class,
                        mock(JsonDeserializationContext.class)));
        assertEquals(new Location(targetWorld, -1.1241, 6.14, 0.1, -4, 3.56f),
                locationDeserializer.deserialize(new JsonPrimitive(" -1.1241: 6.14: 0.1:  -4:3.56"), Location.class,
                        mock(JsonDeserializationContext.class)));
    }
}
