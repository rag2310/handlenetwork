package com.rago.handlenetwork.data.usecases

import arrow.core.Either
import com.rago.handlenetwork.data.model.ErrorResponse
import com.rago.handlenetwork.data.model.SuccessResponse
import com.rago.handlenetwork.data.model.Task
import com.rago.handlenetwork.data.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository
) : TaskUseCase {
    override fun executeGetTask(): Flow<Either<SuccessResponse<List<Task>>, ErrorResponse>> {
        return taskRepository.getTask()
    }
}