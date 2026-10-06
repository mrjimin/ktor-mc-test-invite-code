package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    // EngineMain.main(args)
    embeddedServer(Netty, port = 8080) {
        configure()
    }.start(wait = true)
}
