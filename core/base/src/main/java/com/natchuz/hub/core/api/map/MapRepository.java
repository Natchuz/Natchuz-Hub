package com.natchuz.hub.core.api.map;

import java.io.InputStream;
import java.util.Optional;

/**
 * Represents map repository
 */
public interface MapRepository {

    /**
     * Request map from repo
     *
     * @param id id of map
     * @return Optional InputStream of map file content or empty if map with matching id was not found
     */
    Optional<InputStream> requestMap(String id);

}
