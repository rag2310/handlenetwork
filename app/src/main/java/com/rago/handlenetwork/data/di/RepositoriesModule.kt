package com.rago.handlenetwork.data.di

import com.rago.handlenetwork.data.repositories.TaskRepository
import com.rago.handlenetwork.data.repositories.TaskRepositoryImpl
import com.rago.handlenetwork.data.service.TaskApiService
import com.rago.handlenetwork.data.utils.network.RetrofitUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        retrofitUtils: RetrofitUtils,
        taskApiService: TaskApiService
    ): TaskRepository {
        return TaskRepositoryImpl(retrofitUtils, taskApiService)
    }
}