package com.natchuz.hub.core.api.map;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see MapManifest
 */
public class ManifestParsingTest {

    @Test
    public void testLoadingFromString() {
        MapManifest mapManifest = MapManifest.fromString("id = namespace/testMap\n" +
                "name = Test Map Name\n" +
                "description = Nice map\n" +
                "authors = Natchuz, somerandomguy , hypixelispaytowin");

        assertEquals("namespace/testMap", mapManifest.getId());
        assertEquals("Test Map Name", mapManifest.getName());
        assertEquals("Nice map", mapManifest.getDescription());
        assertEquals(Arrays.asList("Natchuz", "somerandomguy", "hypixelispaytowin"), mapManifest.getAuthors());
    }
}
