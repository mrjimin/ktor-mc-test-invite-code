package com.example.domain.invite

import com.example.domain.user.UserInfo
import com.example.domain.user.UserRepository

class InviteService(
    private val inviteRepository: InviteRepository,
    private val userRepository: UserRepository,
) {

    fun create(userId: UInt): Invite? {
        if (inviteRepository.findByUserId(userId) != null) {
            return null
        }

        return inviteRepository.create(userId)
    }

    fun findByCode(code: InviteCode): Invite? {
        return inviteRepository.findByCode(code)
    }

    fun findUserByCode(code: InviteCode): UserInfo? {
        val invite = inviteRepository.findByCode(code)
            ?: return null

        return userRepository.findById(invite.userId)
    }

    fun delete(code: InviteCode) {
        inviteRepository.delete(code)
    }
}
