package com.rago.handlenetwork.data.di

import com.rago.handlenetwork.data.service.TaskApiService
import com.rago.handlenetwork.data.utils.Constants
import com.rago.handlenetwork.data.utils.network.RetrofitUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .writeTimeout(Duration.ofSeconds(60))
            .callTimeout(Duration.ofSeconds(60))
            .readTimeout(Duration.ofSeconds(60))
            .connectTimeout(Duration.ofSeconds(60))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskApiService(
        retrofit: Retrofit
    ): TaskApiService {
        return retrofit.create(TaskApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitUtils(): RetrofitUtils {
        return RetrofitUtils()
    }
}