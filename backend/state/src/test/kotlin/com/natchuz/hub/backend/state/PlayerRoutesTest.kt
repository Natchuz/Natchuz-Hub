package com.natchuz.hub.backend.state

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class PlayerRoutesTest {

    @Test
    fun `test flags route`() = withTestApplication({
        val service = mock<Service> {
            on { getPlayerFlags(eqUUID(player1)) } doReturn listOf(PlayerFlags.PROXY_JOIN)
            on { getPlayerFlags(eqUUID(player2)) } doReturn emptyList()
            on { getPlayerFlags(eqUUID(player3)) } doReturn emptyList()
            on { getPlayerFlags(eqUUID(player4)) } doReturn listOf(PlayerFlags.PROXY_JOIN)
            on { getPlayerFlags(eqUUID(player5)) } doReturn listOf(PlayerFlags.PROXY_JOIN)
        }
        mainWithService(service)
    }) {
        fun request(uuid: String): TestApplicationCall = handleRequest {
            method = HttpMethod.Get
            uri = "/player/$uuid/flags"
        }

        assertEqualsJson("""["PROXY_JOIN"]""", request(player1).response.content!!)
        assertEqualsJson("""[]""", request(player2).response.content!!)
        assertEqualsJson("""[  ]""", request(player3).response.content!!)
        assertEqualsJson("""[ "PROXY_JOIN" ]""", request(player4).response.content!!)
        assertEqualsJson(""" [ "PROXY_JOIN" ] """, request(player5).response.content!!)
    }

    @Test
    fun `test send route`() = withTestApplication({
        mainWithService(mock<Service>())
    }) {
        fun request(uuid: String, targetServer: String): TestApplicationCall = handleRequest {
            method = HttpMethod.Post
            uri = "/player/$uuid/send"
            addHeader("Content-Type", "application/json")
            setBody(""" { "targetServer" : "$targetServer", "flags" : [] } """)
        }

        assertEqualsJson("""{}""", request(player1, "lb").response.content!!)
        assertEqualsJson("""{ }""", request(player2, "lb").response.content!!)
        assertEqualsJson("""{  }""", request(player3, "lb").response.content!!)
        assertEqualsJson("""{}""", request(player4, "lb").response.content!!)
        assertEqualsJson("""{ }""", request(player5, "lb").response.content!!)
    }


    @Test
    fun `test logout route`() = withTestApplication({
        val service = mock<Service> {
            on { logoutPlayer(any()) } doReturn true
        }
        mainWithService(service)
    }) {
        fun request(uuid: String): TestApplicationCall = handleRequest {
            method = HttpMethod.Post
            uri = "/player/$uuid/logout"
        }

        assertEqualsJson("""{}""", request(player1).response.content!!)
        assertEqualsJson("""{ }""", request(player2).response.content!!)
        assertEqualsJson("""{  }""", request(player3).response.content!!)
        assertEqualsJson("""{}""", request(player4).response.content!!)
        assertEqualsJson("""{ }""", request(player5).response.content!!)
    }

    @Test
    fun `test login route`() = withTestApplication({
        val service = mock<Service> {
            on { loginPlayer(eqUUID(player1)) } doReturn PlayerLoginStatus.Ok("server")
            on { loginPlayer(eqUUID(player2)) } doReturn PlayerLoginStatus.Ban("Ban, because yes")
            on { loginPlayer(eqUUID(player3)) } doReturn PlayerLoginStatus.Other("undefined error")
            on { loginPlayer(eqUUID(player4)) } doReturn PlayerLoginStatus.Maintenance()
            on { loginPlayer(eqUUID(player5)) } doReturn PlayerLoginStatus.Ok("other server")
        }
        mainWithService(service)
    }) {
        fun request(uuid: String): TestApplicationCall = handleRequest {
            method = HttpMethod.Post
            uri = "/player/$uuid/login"
        }

        assertEqualsJson("""{ "status" : "ok", "targetServer" : "server" }""",
                request(player1).response.content!!)
        assertEqualsJson("""{ "reason" : "Ban, because yes", "status" : "ban" }""",
                request(player2).response.content!!)
        assertEqualsJson("""{ "status" : "other", "info" : "undefined error" }""",
                request(player3).response.content!!)
        assertEqualsJson("""{ "status" : "maintenance" }""",
                request(player4).response.content!!)
        assertEqualsJson("""{ "status" : "ok", "targetServer" : "other server" }""",
                request(player5).response.content!!)
    }

    companion object {
        const val player1 = "db6d37f8-b46d-40f6-9ab7-eead65dbc207"
        const val player2 = "f1654231-7139-4eb4-afbd-5fb82fc5288d"
        const val player3 = "d71bf669-46b8-4ed4-af20-2f0ebe01344b"
        const val player4 = "b3177aba-7c34-4192-84d9-ab093d1bdff3"
        const val player5 = "cf9f9327-b99d-4f7a-bfef-c6e9445a20c8"
    }

    private fun assertEqualsJson(expected: String, actual: String) = with(Json) {
        assertEquals(parseToJsonElement(expected), parseToJsonElement(actual))
    }

    private fun eqUUID(uuid: String) = eq(UUID.fromString(uuid))
}