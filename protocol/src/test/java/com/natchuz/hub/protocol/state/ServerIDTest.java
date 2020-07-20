package com.natchuz.hub.protocol.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerIDTest {

    @Test
    public void testParsing() {
        String input = "gamemode/subgamemode/server";

        ServerID serverID = ServerID.fromString(input);

        assertEquals(serverID.getId(), "server");
        assertEquals(serverID.getNamespaces().get(0), "gamemode");
        assertEquals(serverID.getNamespaces().get(1), "subgamemode");

        assertEquals(serverID.getFullNamespace(), "gamemode/subgamemode");
        assertEquals(serverID.getFull(), "gamemode/subgamemode/server");

    }
}
