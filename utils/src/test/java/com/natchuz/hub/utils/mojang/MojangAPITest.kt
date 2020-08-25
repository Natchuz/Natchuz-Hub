package com.natchuz.hub.utils.mojang

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.*

class MojangAPITest {

    @Test
    fun `test client`() {
        val api = ElectroidMojangAPI(client = HttpClient(MockEngine) {
            installJson()
            engine {
                addHandler {
                    respond(Companion.response, headers = headersOf("Content-Type", "application/json"))
                }
            }
        })

        val username = api.getUsername(UUID.fromString("fe2d7c4f-28e5-4149-9191-d53de421e182"))
        assertEquals("Natchuz", username)

        val uuid = api.getUUID("Natchuz")
        assertEquals(UUID.fromString("fe2d7c4f-28e5-4149-9191-d53de421e182"), uuid)
    }

    @Test
    fun `test invalid`() {
        val api = ElectroidMojangAPI(client = HttpClient(MockEngine) {
            installJson()
            engine {
                addHandler {
                    respond("", status = HttpStatusCode.NotFound,
                            headers = headersOf("Content-Type", "application/json"))
                }
            }
        })

        val username = api.getUsername(UUID.fromString("fe2d7c4f-28e5-4149-9191-d53de421e182"))
        assertNull(username)

        val uuid = api.getUUID("Natchuz")
        assertNull(uuid)
    }

    companion object {
        const val response = """
            {
              "uuid": "fe2d7c4f-28e5-4149-9191-d53de421e182",
              "username": "Natchuz",
              "username_history": [
                {
                  "username": "Natchuz"
                }
              ],
              "created_at": "2015-12-13"
            }
        """
    }
}