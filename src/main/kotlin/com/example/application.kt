package com.example

import com.example.domain.discord.configureDiscordOAuth
import io.ktor.server.application.*

fun Application.configure() {
    configureKoin()
    configureSerialization()
    configureExposed()
    configureRouting()
    configureDiscordOAuth()
}