package com.example

import com.example.domain.invite.inviteModule
import com.example.domain.user.userModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            userModule(),
            inviteModule(),
        )
    }
}