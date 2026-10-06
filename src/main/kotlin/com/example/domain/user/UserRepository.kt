package com.example.domain.user

import kotlin.uuid.Uuid

interface UserRepository {
    fun create(command: UserCreate): UserInfo
    fun findById(id: UInt): UserInfo?
    fun findByUuid(uuid: Uuid): UserInfo?
    fun delete(id: UInt)
}
