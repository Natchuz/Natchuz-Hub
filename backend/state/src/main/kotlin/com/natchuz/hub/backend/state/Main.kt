package com.natchuz.hub.backend.state

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*

/** Entry point used to construct services and pass them to [mainWithService] */
fun Application.main() {
    val service = DefaultService.construct(
            redisAddress = "redis"
    )

    mainWithService(service)
}

fun Application.mainWithService(service: Service) {
    installJson()
    installPages()

    routing {
        playerRoutes(service)
    }
}

/** Install JSON serializers */
fun Application.installJson() = install(ContentNegotiation) {
    json(MessagesJson)
}

/** Install status pages */
fun Application.installPages() = install(StatusPages) {
    exception<ContentTransformationException> {
        call.respond(status = HttpStatusCode.BadRequest, "Bad request")
    }
}