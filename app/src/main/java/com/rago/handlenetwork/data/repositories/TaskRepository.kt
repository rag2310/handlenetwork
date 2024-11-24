package com.rago.handlenetwork.data.repositories

import arrow.core.Either
import com.rago.handlenetwork.data.model.ErrorResponse
import com.rago.handlenetwork.data.model.SuccessResponse
import com.rago.handlenetwork.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTask(): Flow<Either<SuccessResponse<List<Task>>, ErrorResponse>>
}