package com.example

import com.example.domain.invite.inviteModule
import com.example.domain.user.userModule
import io.ktor.server.application.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            jsonModule(),
            userModule(),
            inviteModule(),
        )
    }
}

fun jsonModule() = module {
    single<Json> { Json {
        ignoreUnknownKeys = true
    } }
}