package com.natchuz.hub.backend.state

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import redis.clients.jedis.Jedis
import java.util.*
import kotlin.test.assertEquals

class DefaultServiceTest {

    @Test
    fun `test login player`() {
        val jedis = mock<Jedis>()
        val service = DefaultService.construct(jedis)

        with(service.loginPlayer(player1.asUUID())) {
            assertEquals(PlayerLoginStatus.Ok("lb"), this)
            verify(jedis)["player.$player1.flags"] = "PROXY_JOIN"
        }
    }

    private fun String.asUUID(): UUID = UUID.fromString(this)

    companion object {
        const val player1 = "db6d37f8-b46d-40f6-9ab7-eead65dbc207"
        const val player2 = "f1654231-7139-4eb4-afbd-5fb82fc5288d"
        const val player3 = "d71bf669-46b8-4ed4-af20-2f0ebe01344b"
        const val player4 = "b3177aba-7c34-4192-84d9-ab093d1bdff3"
        const val player5 = "cf9f9327-b99d-4f7a-bfef-c6e9445a20c8"
    }
}