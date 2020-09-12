package com.natchuz.hub.core.api.map;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Represents map manifest that contains map id, name, description and authors
 */
public class MapManifest {

    private String id;
    private String name;
    private String description;
    private List<String> authors;

    private MapManifest() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAuthors() {
        return authors;
    }

    /**
     * Loads manifest from input stream
     */
    @SneakyThrows
    public static MapManifest fromStream(InputStream stream) {
        Properties properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            throw new Exception("Map manifest is wronly formated!");
        }

        return getManifest(properties);
    }

    /**
     * Loads manifest from string
     */
    @SneakyThrows
    public static MapManifest fromString(String string) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(string));
        } catch (IOException e) {
            throw new Exception("Map manifest is wronly formated!");
        }

        return getManifest(properties);
    }

    private static MapManifest getManifest(Properties properties) {
        MapManifest target = new MapManifest();
        target.id = properties.getProperty("id");
        target.name = properties.getProperty("name");
        target.description = properties.getProperty("description");
        target.authors = Arrays.asList(properties.getProperty("authors").split("\\s*,\\s*"));

        return target;
    }
}
