package com.jhonw.dogedex.di

import com.jhonw.dogedex.machinelearning.ClassifierRepository
import com.jhonw.dogedex.machinelearning.ClassifierTasks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassifierModule {

    @Binds
    abstract fun bindClassfierTasks(
        classifierRepository: ClassifierRepository
    ): ClassifierTasks
}