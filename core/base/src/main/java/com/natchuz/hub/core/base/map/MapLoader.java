package com.natchuz.hub.core.base.map;

import java.io.IOException;

import com.natchuz.hub.core.api.map.LoadedMap;

public interface MapLoader {

    /**
     * Loads map
     *
     * @param id id of map to load
     * @return Load result
     */
    LoadedMap load(String id) throws IOException;

}
