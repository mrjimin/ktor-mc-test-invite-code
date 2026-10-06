package com.example.domain.invite

import com.example.INVITE_CODE_LENGTH
import com.example.domain.user.UserTable
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.date

object InviteTable : UuidTable() {
    val code = varchar("code", INVITE_CODE_LENGTH).uniqueIndex()
    val userId = reference("user_id", UserTable.id).uniqueIndex()
    val createdAt = date("created_at")
}
