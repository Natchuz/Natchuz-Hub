package com.natchuz.hub.utils;

import java.util.Objects;
import java.util.UUID;

/**
 * Utils to convert UUIDs
 */
public class UUIDConverter {

    /**
     * Convert given UUID to condensed string (without dashes)
     *
     * @param uuid uuid convert to
     * @return converted string
     */
    public static String toCondensed(UUID uuid) {
        Objects.requireNonNull(uuid, "UUID cannot be null!");
        return uuid.toString().replace("-", "");
    }

    /**
     * Creates UUID from condensed string
     *
     * @param uuid condensed string
     * @return converted UUID
     */
    public static UUID fromCondensed(String uuid) {
        return UUID.fromString(uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
}
