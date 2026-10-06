package com.example.domain.invite

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.Clock

class InviteRepositoryImpl : InviteRepository {

    override fun create(userId: UInt): Invite = transaction {
        val code = InviteCodeGenerator.generate()

        val createdAt = Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date

        InviteTable.insert {
            it[InviteTable.code] = code.value
            it[InviteTable.userId] = userId
            it[InviteTable.createdAt] = createdAt
        }

        Invite(
            code = code,
            userId = userId,
            createdAt = createdAt,
        )
    }

    override fun findByCode(code: InviteCode): Invite? = transaction {
        InviteTable
            .selectAll()
            .where {
                InviteTable.code eq code.value
            }
            .singleOrNull()
            ?.toInvite()
    }

    override fun findByUserId(userId: UInt): Invite? = transaction {
        InviteTable
            .selectAll()
            .where {
                InviteTable.userId eq userId
            }
            .singleOrNull()
            ?.toInvite()
    }

    override fun delete(code: InviteCode): Unit = transaction {
        InviteTable.deleteWhere {
            InviteTable.code eq code.value
        }
    }
}

private fun ResultRow.toInvite(): Invite {
    return Invite(
        code = InviteCode(this[InviteTable.code]),
        userId = this[InviteTable.userId].value,
        createdAt = this[InviteTable.createdAt],
    )
}
