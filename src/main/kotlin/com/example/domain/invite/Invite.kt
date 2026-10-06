package com.example.domain.invite

import com.example.INVITE_CODE_LENGTH
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Invite(
    val code: InviteCode,
    val userId: UInt,
    val createdAt: LocalDate,
)

@JvmInline
@Serializable
value class InviteCode(
    val value: String,
) {
    init {
        require(value.length == INVITE_CODE_LENGTH)
    }
}
