package com.rago.handlenetwork.data.di

import com.rago.handlenetwork.data.repositories.TaskRepository
import com.rago.handlenetwork.data.usecases.TaskUseCase
import com.rago.handlenetwork.data.usecases.TaskUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @Provides
    @ViewModelScoped
    fun provideTaskUseCase(
        taskRepository: TaskRepository
    ): TaskUseCase {
        return TaskUseCaseImpl(taskRepository)
    }
}