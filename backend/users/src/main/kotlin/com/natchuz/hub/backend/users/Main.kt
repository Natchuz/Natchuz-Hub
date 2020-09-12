package com.natchuz.hub.backend.users

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

/**
 * Entry point for service
 */
fun Application.main() {
    val service = DefaultService.construct(
            connectionUrl = "mongodb://mongo:27017",
            databaseName = "users",
            usersCollectionName = "users",
    )

    mainWithService(service)
}

fun Application.mainWithService(service: Service) {
    installPages()

    routing {
        installRoutes(service)
    }
}

/** Install status pages */
fun Application.installPages() = install(StatusPages) {
    exception<ContentTransformationException> {
        call.respond(status = HttpStatusCode.BadRequest, "Bad request")
    }
}