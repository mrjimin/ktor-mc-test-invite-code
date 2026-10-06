package com.example.domain.user

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.uuid.Uuid

class UserRepositoryImpl : UserRepository {

    override fun create(command: UserCreate): UserInfo = transaction {
        val statement = UserTable.insert {
            it[UserTable.name] = command.name
            it[UserTable.uuid] = command.uuid
        }

        val id = statement[UserTable.id].value

        UserInfo(
            id = id,
            name = command.name,
            uuid = command.uuid,
        )
    }

    override fun findById(id: UInt): UserInfo? = transaction {
        UserTable
            .selectAll()
            .where {
                UserTable.id eq id
            }
            .singleOrNull()
            ?.toUserInfo()
    }

    override fun findByUuid(uuid: Uuid): UserInfo? = transaction {
        UserTable
            .selectAll()
            .where {
                UserTable.uuid eq uuid
            }
            .singleOrNull()
            ?.toUserInfo()
    }

    override fun delete(id: UInt): Unit = transaction {
        UserTable.deleteWhere {
            UserTable.id eq id
        }
    }
}

private fun ResultRow.toUserInfo(): UserInfo {
    return UserInfo(
        id = this[UserTable.id].value,
        name = this[UserTable.name],
        uuid = this[UserTable.uuid],
    )
}
