package com.natchuz.hub.core.context;

import com.natchuz.hub.core.api.PrivilegedFacade;

/**
 * Loads context from Java System Properties
 */
public class PropertiesContextLoader implements ContextLoader {

    private final PrivilegedFacade facade;

    public PropertiesContextLoader(PrivilegedFacade facade) {
        this.facade = facade;
    }

    @Override
    public ServerContext loadContext() {
        String contextName = System.getProperty("server.context");

        if (contextName.equals("network")) {
            return new NetworkContext(facade);
        }

        if (contextName.equals("standalone")) {
            return new StandaloneContext(facade);
        }

        throw new RuntimeException("No server context was provided!");
    }
}
