package com.natchuz.hub.bungeecord.handlers

import com.natchuz.hub.bungeecord.installSerializers
import com.nhaarman.mockitokotlin2.*
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.*
import io.ktor.client.utils.*
import io.ktor.http.*
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ServerConnectEvent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.check

class ServerConnectHandlerTest {

    private lateinit var serverConnectHandler: ServerConnectHandler
    private lateinit var player: ProxiedPlayer
    private lateinit var proxy: ProxyServer
    private val playerUUID: UUID = UUID.fromString("2d3df994-e0b6-47cf-b358-a84ecc5eae5a")

    private fun setup(content: String) {
        val httpClient = HttpClient(MockEngine) {
            installSerializers()
            engine {
                addHandler { respond(content, headers = headersOf("Content-Type", "application/json")) }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 1000
            }
        }
        player = mock {
            on { uniqueId } doReturn playerUUID
        }
        proxy = mock()
        serverConnectHandler = spy(ServerConnectHandler(httpClient, proxy))
    }

    @Test
    fun `test join proxy with with ok status`() {
        setup("""{ "status": "ok", "targetServer": "test_server" }""")

        val event = spy(
            ServerConnectEvent(player, null, ServerConnectEvent.Reason.JOIN_PROXY)
        )

        val serverInfo = mock<ServerInfo>()
        whenever(proxy.getServerInfo("test_server")).thenReturn(serverInfo)

        serverConnectHandler.onJoin(event)
        verify(event).target = serverInfo
    }

    @Test
    fun `test join proxy with with ban status`() {
        setup("""{ "status": "ban", "reason": "Ban, because yes" }""")
        val event = ServerConnectEvent(player, null, ServerConnectEvent.Reason.JOIN_PROXY)

        serverConnectHandler.onJoin(event)
        verify(player).disconnect(eq("Ban, because yes"))
    }
}