package com.example.domain.user

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlin.uuid.Uuid

fun Route.userRoutes() {
    val userService by inject<UserService>()

    route("/users") {

        post {
            val request = call.receive<UserCreate>()

            val user = userService.create(request)

            call.respond(
                HttpStatusCode.Created,
                user,
            )
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toUIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest)

            val user = userService.findById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respond(user)
        }

        get("/uuid/{uuid}") {
            val uuid = call.parameters["uuid"]?.let {
                runCatching { Uuid.parse(it) }.getOrNull()
            } ?: return@get call.respond(HttpStatusCode.BadRequest)

            val user = userService.findByUuid(uuid)
                ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respond(user)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toUIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest)

            userService.delete(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
