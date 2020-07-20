package com.natchuz.hub.core.map;

import java.io.IOException;

public interface MapLoader {

    /**
     * Loads map
     *
     * @param id id of map to load
     * @return Load result
     */
    LoadedMap load(String id) throws IOException;

}
