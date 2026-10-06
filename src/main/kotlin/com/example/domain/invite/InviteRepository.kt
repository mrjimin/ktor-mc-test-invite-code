package com.example.domain.invite

interface InviteRepository {
    fun create(userId: UInt): Invite
    fun findByCode(code: InviteCode): Invite?
    fun findByUserId(userId: UInt): Invite?
    fun delete(code: InviteCode)
}

