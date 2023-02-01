package com.jhonw.dogedex.di

import com.jhonw.dogedex.doglist.DogRepository
import com.jhonw.dogedex.doglist.DogTasks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DogTasksModule {

    @Binds
    abstract fun bindDogTaks(
        dogRepository: DogRepository
    ): DogTasks// enlaza esta interface con la imprementacion de repositorio
}