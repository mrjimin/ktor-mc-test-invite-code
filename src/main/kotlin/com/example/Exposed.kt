package com.example

import com.example.domain.invite.InviteTable
import com.example.domain.user.UserTable
import io.ktor.server.application.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.configureExposed() {

    Database.connect(
        url = "jdbc:h2:file:./h2",
        driver = "org.h2.Driver",
    )

    transaction {
        SchemaUtils.create(
            UserTable,
            InviteTable,
        )
    }
}
