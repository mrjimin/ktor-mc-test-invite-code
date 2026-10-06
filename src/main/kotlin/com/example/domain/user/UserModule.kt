package com.example.domain.user

import org.koin.dsl.module

fun userModule() = module {
    single<UserRepository> { UserRepositoryImpl() }
    single { UserService(get()) }
}
