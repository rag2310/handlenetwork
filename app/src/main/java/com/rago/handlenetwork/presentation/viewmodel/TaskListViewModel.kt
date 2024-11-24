package com.rago.handlenetwork.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rago.handlenetwork.data.usecases.TaskUseCase
import com.rago.handlenetwork.presentation.uistate.TaskListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskUseCase: TaskUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<TaskListUIState> = MutableStateFlow(
        TaskListUIState(
            getTasks = ::getTasks,
            setOnShowLoading = ::setOnShowLoading,
            setOnHideLoading = ::setOnHideLoading
        )
    )
    val uiState: StateFlow<TaskListUIState> = _uiState.asStateFlow()

    private fun getTasks() {
        viewModelScope.launch {
            uiState.value.onShowLoading()
            val response = taskUseCase.executeGetTask().first()

            response.fold({ success ->
                _uiState.update {
                    it.copy(tasks = success.data ?: listOf())
                }
                uiState.value.onHideLoading()
            }, { error ->
                Log.i("TAG", "getTasks: ${error.error}")
                uiState.value.onHideLoading()
            })
        }
    }

    private fun setOnShowLoading(onShowLoading: () -> Unit) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    onShowLoading = onShowLoading
                )
            }
        }
    }

    private fun setOnHideLoading(onHideLoading: () -> Unit) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    onHideLoading = onHideLoading
                )
            }
        }
    }
}