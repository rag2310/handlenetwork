package com.rago.handlenetwork.presentation.uistate

import com.rago.handlenetwork.data.model.Task

data class TaskListUIState(
    val tasks: List<Task> = listOf(),
    val getTasks: () -> Unit = {},
    val setOnShowLoading: (() -> Unit) -> Unit = {},
    val setOnHideLoading: (() -> Unit) -> Unit = {},
    val onShowLoading: () -> Unit = {},
    val onHideLoading: () -> Unit = {}
)