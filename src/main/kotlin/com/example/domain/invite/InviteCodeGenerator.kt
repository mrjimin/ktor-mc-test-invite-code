package com.example.domain.invite

import com.example.INVITE_CODE_LENGTH
import kotlin.random.Random

object InviteCodeGenerator {

    private const val CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"

    fun generate(length: Int = INVITE_CODE_LENGTH, random: Random = Random.Default): InviteCode {
        val code = buildString(length) {
            repeat(length) {
                append(CHARS[random.nextInt(CHARS.length)])
            }
        }
        return InviteCode(code)
    }
}