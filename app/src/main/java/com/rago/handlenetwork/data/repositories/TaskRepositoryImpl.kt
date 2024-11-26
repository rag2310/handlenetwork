package com.rago.handlenetwork.data.repositories

import arrow.core.Either
import com.rago.handlenetwork.data.model.ErrorResponse
import com.rago.handlenetwork.data.model.SuccessResponse
import com.rago.handlenetwork.data.model.Task
import com.rago.handlenetwork.data.service.TaskApiService
import com.rago.handlenetwork.data.utils.network.RetrofitUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val retrofitUtils: RetrofitUtils,
    private val taskApiService: TaskApiService
) : TaskRepository {
    override fun getTask(): Flow<Either<SuccessResponse<List<Task>>, ErrorResponse>> {
        return retrofitUtils.arrow(taskApiService.getTasks())
    }
}