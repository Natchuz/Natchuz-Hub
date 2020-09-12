package com.natchuz.hub.backend.users

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import java.util.*

fun Routing.installRoutes(service: Service) = route("{playerUUID}") {


    fun PipelineContext<Unit, ApplicationCall>.getPlayer(): UUID {
        return UUID.fromString(call.parameters["playerUUID"])
    }

    get {
        call.respond(service.getUser(getPlayer()))
    }

    route("/friends") {
        post("/invite") {

        }

        post("/accept") {

        }

        post("/decline") {

        }

        post("/remove") {

        }
    }
}