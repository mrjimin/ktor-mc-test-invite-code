package com.example.domain.user

import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable

object UserTable : UIntIdTable() {
    val name = varchar("name", 32)
    val uuid = uuid("uuid").uniqueIndex()
}