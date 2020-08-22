package com.natchuz.hub.backend.state

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.features.ContentTransformationException
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json

fun Application.setup() {
    install(ContentNegotiation) {
        json(Json(DefaultJson) {
            prettyPrint = true
            useArrayPolymorphism = false
            classDiscriminator = "status"
        })
    }

    install(StatusPages) {
        exception<ContentTransformationException> {
            call.respond(status = HttpStatusCode.BadRequest, "Bad request")
        }
    }
}

fun Application.login() = routing {
    route("/player") {
        post("/login") {
            call.respond(PlayerLoginResponse.OkStatus("lb"))
        }

        post("/logout") {
            call.respond(Unit)
        }
    }
}