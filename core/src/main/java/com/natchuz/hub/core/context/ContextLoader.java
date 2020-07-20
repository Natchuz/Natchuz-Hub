package com.natchuz.hub.core.context;

/**
 * Loads context in which server is currently running from various sources
 */
public interface ContextLoader {

    /**
     * Returns context
     */
    ServerContext loadContext();

}
