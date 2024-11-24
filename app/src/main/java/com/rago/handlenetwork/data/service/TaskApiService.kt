package com.rago.handlenetwork.data.service

import com.rago.handlenetwork.data.model.Task
import retrofit2.Call
import retrofit2.http.GET


interface TaskApiService {

    @GET("tasks")
    fun getTasks(): Call<List<Task>>
}