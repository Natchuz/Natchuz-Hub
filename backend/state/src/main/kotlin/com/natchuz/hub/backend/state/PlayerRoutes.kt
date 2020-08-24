package com.natchuz.hub.backend.state

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import java.util.*

fun Routing.playerRoutes(service: Service) = route("/player/{playerUUID}") {

    post("/login") {
        call.respond(service.loginPlayer(player))
    }

    post("/logout") {
        call.respond(Unit)
    }

    post("/send") {
        call.respond(Unit)
    }

    get("/flags") {
        call.respond(service.getPlayerFlags(player))
    }
}

private val PipelineContext<Unit, ApplicationCall>.player : UUID
    get() {
        return UUID.fromString(call.parameters["playerUUID"])
    }