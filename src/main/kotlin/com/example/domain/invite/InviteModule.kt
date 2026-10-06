package com.example.domain.invite

import org.koin.dsl.module

fun inviteModule() = module {
    single <InviteRepository> { InviteRepositoryImpl() }
    single { InviteService(get(), get()) }
}