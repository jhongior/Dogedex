package com.jhonw.dogedex.di

import com.jhonw.dogedex.auth.AuthRepository
import com.jhonw.dogedex.auth.AuthTasks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthTasksModule {

    @Binds
    abstract fun bindAuthTasks(
        authRepository: AuthRepository
    ): AuthTasks
}