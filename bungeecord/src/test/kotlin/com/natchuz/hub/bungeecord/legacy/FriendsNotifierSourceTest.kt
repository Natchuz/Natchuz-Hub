package com.natchuz.hub.bungeecord.legacy

import com.natchuz.hub.protocol.messaging.MessageEndpoint
import com.natchuz.hub.protocol.messaging.Protocol
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import java.util.*

class FriendsNotifierSourceTest {

    private lateinit var player: ProxiedPlayer
    private lateinit var protocol: Protocol
    private lateinit var friendsNotifier: FriendsNotifierSource

    private val playerUUID: UUID = UUID.fromString("2d3df994-e0b6-47cf-b358-a84ecc5eae5a")

    @BeforeEach
    fun setup() {
        player = mock {
            on { uniqueId } doReturn playerUUID
        }
        protocol = mock()
        friendsNotifier = FriendsNotifierSource(protocol)
    }

    @Test
    fun `test if join event triggered notification`() {
        val connectEvent = mock<ServerConnectEvent> {
            on { reason } doReturn ServerConnectEvent.Reason.JOIN_PROXY
            on { player } doReturn player
        }

        friendsNotifier.onPlayerJoin(connectEvent)
        verify(protocol).send(ArgumentMatchers.any(MessageEndpoint::class.java), ArgumentMatchers.eq("joinEvent"),
                ArgumentMatchers.eq("2d3df994e0b647cfb358a84ecc5eae5a"))
    }

    @Test
    fun `test if quit event triggered notification`() {
        val disconnectEvent = mock<PlayerDisconnectEvent> {
            on { player } doReturn player
        }

        friendsNotifier.onPlayerQuit(disconnectEvent)
        verify(protocol).send(ArgumentMatchers.any(MessageEndpoint::class.java), ArgumentMatchers.eq("leftEvent"),
                ArgumentMatchers.eq("2d3df994e0b647cfb358a84ecc5eae5a"))
    }
}