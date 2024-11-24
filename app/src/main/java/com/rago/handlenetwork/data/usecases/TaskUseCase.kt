package com.rago.handlenetwork.data.usecases

import arrow.core.Either
import com.rago.handlenetwork.data.model.ErrorResponse
import com.rago.handlenetwork.data.model.SuccessResponse
import com.rago.handlenetwork.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskUseCase {
    fun executeGetTask(): Flow<Either<SuccessResponse<List<Task>>, ErrorResponse>>
}