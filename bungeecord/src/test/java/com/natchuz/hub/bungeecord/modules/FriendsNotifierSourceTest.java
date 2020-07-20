package com.natchuz.hub.bungeecord.modules;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import com.natchuz.hub.protocol.messaging.MessageEndpoint;
import com.natchuz.hub.protocol.messaging.Protocol;

import static org.mockito.Mockito.*;

public class FriendsNotifierSourceTest {

    @Mock
    private Protocol protocol;
    @Mock
    private ProxiedPlayer player;
    private UUID playerUUID;

    private FriendsNotifierSource friendsNotifier;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        playerUUID = UUID.fromString("2d3df994-e0b6-47cf-b358-a84ecc5eae5a");
        when(player.getUniqueId()).thenReturn(playerUUID);

        friendsNotifier = new FriendsNotifierSource(protocol);
    }

    @Test
    public void testJoinEvent() {
        ServerConnectEvent connectEvent = mock(ServerConnectEvent.class);
        when(connectEvent.getReason()).thenReturn(ServerConnectEvent.Reason.JOIN_PROXY);
        when(connectEvent.getPlayer()).thenReturn(player);

        friendsNotifier.onPlayerJoin(connectEvent);

        verify(protocol).send(any(MessageEndpoint.class), eq("joinEvent"),
                eq("2d3df994e0b647cfb358a84ecc5eae5a"));
    }

    @Test
    public void testQuitEvent() {
        PlayerDisconnectEvent disconnectEvent = mock(PlayerDisconnectEvent.class);
        when(disconnectEvent.getPlayer()).thenReturn(player);

        friendsNotifier.onPlayerQuit(disconnectEvent);

        verify(protocol).send(any(MessageEndpoint.class), eq("leftEvent"),
                eq("2d3df994e0b647cfb358a84ecc5eae5a"));
    }

}
