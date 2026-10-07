package com.example.domain.discord

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.collections.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

private const val DISCORD_AUTHORIZE_URL =
    "https://discord.com/oauth2/authorize"

private const val DISCORD_TOKEN_URL =
    "https://discord.com/api/oauth2/token"

private const val DISCORD_USER_URL =
    "https://discord.com/api/users/@me"

private const val SESSION_NAME = "user_session"

val applicationHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

fun Application.configureDiscordOAuth(
    httpClient: HttpClient = applicationHttpClient
) {
    val clientId = System.getenv("DISCORD_CLIENT_ID")
        ?: error("DISCORD_CLIENT_ID is not set")

    val clientSecret = System.getenv("DISCORD_CLIENT_SECRET")
        ?: error("DISCORD_CLIENT_SECRET is not set")

    val redirects = ConcurrentMap<String, String>()

    install(Sessions) {
        cookie<UserSession>(SESSION_NAME)
    }

    install(Authentication) {
        oauth("discord") {
            urlProvider = {
                "http://localhost:8080/callback"
            }

            settings = OAuthServerSettings.OAuth2ServerSettings(
                name = "discord",
                authorizeUrl = DISCORD_AUTHORIZE_URL,
                accessTokenUrl = DISCORD_TOKEN_URL,
                requestMethod = HttpMethod.Post,
                clientId = clientId,
                clientSecret = clientSecret,
                defaultScopes = listOf(
                    "identify",
                    "email"
                ),
                onStateCreated = { call, state ->
                    call.request
                        .queryParameters["redirectUrl"]
                        ?.let { redirects[state] = it }
                }
            )

            client = httpClient

            fallback = { cause ->
                respond(
                    HttpStatusCode.Unauthorized,
                    JsonResponse(
                        success = false,
                        message = cause.message
                    )
                )
            }
        }
    }

    routing {
        authenticate("discord") {
            get("/login") {}
            get("/callback") {
                val oauth = call.principal<OAuthAccessTokenResponse.OAuth2>()
                    ?: return@get call.respond(
                        HttpStatusCode.Unauthorized,
                        JsonResponse(
                            success = false,
                            message = "Discord authentication failed"
                        )
                    )

                val redirectUrl = redirects.remove(oauth.state)

                call.sessions.set(
                    UserSession(
                        token = oauth.accessToken
                    )
                )

                if (redirectUrl != null) {
                    call.respondRedirect(redirectUrl)
                    return@get
                }

                call.respond(
                    DiscordLoginResponse(
                        success = true,
                        message = "Discord login successful",
                        user = getDiscordUser(httpClient, oauth.accessToken)
                    )
                )
            }
        }

        get("/") {
            call.respond(
                JsonResponse(
                    success = true,
                    message = "Discord OAuth server is running"
                )
            )
        }

        get("/home") {
            val user = requireUser(call, httpClient)
                ?: return@get

            call.respond(
                DiscordLoginResponse(
                    success = true,
                    message = "Welcome home!",
                    user = user
                )
            )
        }

        get("/me") {
            val user = requireUser(call, httpClient)
                ?: return@get

            call.respond(user)
        }

        get("/logout") {
            call.sessions.clear<UserSession>()

            call.respond(
                JsonResponse(
                    success = true,
                    message = "Logged out successfully"
                )
            )
        }
    }
}

private suspend fun requireUser(
    call: ApplicationCall,
    httpClient: HttpClient
): DiscordUser? {
    val session = call.sessions.get<UserSession>()
        ?: run {
            call.respond(
                HttpStatusCode.Unauthorized,
                JsonResponse(
                    success = false,
                    message = "Not authenticated"
                )
            )
            return null
        }

    return getDiscordUser(
        httpClient = httpClient,
        accessToken = session.token
    )
}

private suspend fun getDiscordUser(
    httpClient: HttpClient,
    accessToken: String
): DiscordUser {
    return httpClient
        .get(DISCORD_USER_URL) {
            bearerAuth(accessToken)
        }
        .body()
}

@Serializable
data class JsonResponse(
    val success: Boolean,
    val message: String
)

@Serializable
data class DiscordLoginResponse(
    val success: Boolean,
    val message: String,
    val user: DiscordUser
)

@Serializable
data class UserSession(
    val token: String
)

@Serializable
data class DiscordUser(
    val id: String,
    val username: String,

    @SerialName("global_name")
    val globalName: String? = null,

    val discriminator: String? = null,
    val avatar: String? = null,
    val email: String? = null,
    val verified: Boolean? = null
)
