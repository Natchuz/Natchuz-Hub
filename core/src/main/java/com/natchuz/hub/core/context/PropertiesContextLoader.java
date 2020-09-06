package com.natchuz.hub.core.context;

/**
 * Loads context from Java System Properties
 */
public class PropertiesContextLoader implements ContextLoader {

    @Override
    public ServerContext loadContext() {
        String contextName = System.getProperty("server.context");

        if (contextName.equals("network")) {
            return new NetworkContext();
        }

        if (contextName.equals("standalone")) {
            return new StandaloneContext();
        }

        throw new RuntimeException("No server context was provided!");
    }
}
