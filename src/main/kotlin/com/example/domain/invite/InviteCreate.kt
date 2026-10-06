package com.example.domain.invite

import kotlinx.serialization.Serializable

@Serializable
data class InviteCreate(
    val userId: UInt,
)

