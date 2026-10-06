package com.example.domain.invite

import com.example.INVITE_CODE_LENGTH
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InviteCodeGeneratorTest {

    companion object {
        private const val GENERATION_COUNT = 10_000
        private const val ALLOWED_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    }

    @Test
    fun `생성된 코드는 항상 지정된 길이를 가진다`() {
        repeat(GENERATION_COUNT) {
            val code = InviteCodeGenerator.generate(INVITE_CODE_LENGTH)

            assertEquals(INVITE_CODE_LENGTH, code.value.length)
        }
    }

    @Test
    fun `생성된 코드는 허용된 문자만 사용한다`() {
        repeat(GENERATION_COUNT) {
            val code = InviteCodeGenerator.generate(INVITE_CODE_LENGTH)

            assertTrue(
                code.value.all { it in ALLOWED_CHARS },
                "허용되지 않은 문자가 생성되었습니다: ${code.value}"
            )
        }
    }

    @Test
    fun `10_000개의 코드를 생성해도 중복이 발생하지 않는다`() {
        val codes = buildSet {
            repeat(GENERATION_COUNT) {
                add(InviteCodeGenerator.generate(INVITE_CODE_LENGTH))
            }
        }

        assertEquals(GENERATION_COUNT, codes.size)
    }
}