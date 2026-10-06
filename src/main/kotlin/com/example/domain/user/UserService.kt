package com.example.domain.user

import kotlin.uuid.Uuid

class UserService(
    private val userRepository: UserRepository,
) {

    fun create(command: UserCreate): UserInfo {
        return userRepository.create(command)
    }

    fun findById(id: UInt): UserInfo? {
        return userRepository.findById(id)
    }

    fun findByUuid(uuid: Uuid): UserInfo? {
        return userRepository.findByUuid(uuid)
    }

    fun delete(id: UInt) {
        userRepository.delete(id)
    }
}
