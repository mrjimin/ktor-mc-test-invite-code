package com.example.domain.user

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class UserCreate(
    val name: String, // displayName
    val uuid: Uuid, // MC UUID
)
