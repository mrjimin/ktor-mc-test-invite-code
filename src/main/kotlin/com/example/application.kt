package com.example

import io.ktor.server.application.*

fun Application.configure() {
    configureSerialization()
    configureKoin()
    configureExposed()
    configureRouting()
}