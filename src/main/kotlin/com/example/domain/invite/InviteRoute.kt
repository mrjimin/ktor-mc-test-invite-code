package com.example.domain.invite

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.inviteRoutes() {
    val inviteService by inject<InviteService>()

    route("/invites") {

        post {
            val request = call.receive<InviteCreate>()

            val invite = inviteService.create(request.userId)
                ?: return@post call.respond(HttpStatusCode.Conflict)

            call.respond(
                HttpStatusCode.Created,
                invite,
            )
        }

        get("/{code}") {
            val code = call.parameters["code"]
                ?: return@get call.respond(HttpStatusCode.BadRequest)

//            val invite = inviteService.findByCode(
//                InviteCode(code)
//            ) ?: return@get call.respond(HttpStatusCode.NotFound)

            val invite = inviteService.findUserByCode(
                InviteCode(code)
            ) ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respond(invite)
        }

        delete("/{code}") {
            val code = call.parameters["code"]
                ?: return@delete call.respond(HttpStatusCode.BadRequest)

            inviteService.delete(
                InviteCode(code)
            )

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
