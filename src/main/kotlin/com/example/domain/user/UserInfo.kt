package com.example.domain.user

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class UserInfo(
    val id: UInt, // DB ID
    val name: String, // displayName
    val uuid: Uuid, // MC UUID
)