package com.rago.handlenetwork.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rago.handlenetwork.data.model.Priority
import com.rago.handlenetwork.data.model.Task
import com.rago.handlenetwork.data.utils.network.RetrofitUIState
import com.rago.handlenetwork.presentation.uistate.TaskListUIState

@Composable
fun TaskListScreen(taskListUIState: TaskListUIState, retrofitUIState: RetrofitUIState) {
    LaunchedEffect(key1 = Unit) {
        taskListUIState.getTasks()
    }

    LaunchedEffect(key1 = retrofitUIState.refresh) {
        if (retrofitUIState.refresh) {
            taskListUIState.executeCurrentMethods()
        }
    }
    TaskListScreenContent(taskListUIState)
}

@Composable
@Preview(showBackground = true)
private fun TaskListScreenContent(
    taskListUIState: TaskListUIState = TaskListUIState(
        tasks = listOf(
            Task("cleaning", "Clean the house", Priority.Low),
            Task("gardening", "Mow the lawn", Priority.Medium),
            Task("shopping", "Buy the groceries", Priority.High),
            Task("painting", "Paint the fence", Priority.Medium)
        )
    )
) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(taskListUIState.tasks) { task ->
            TaskRow(task)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun TaskRow(task: Task = Task("cleaning", "Clean the house", Priority.Low)) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 5.dp),
        colors = CardDefaults.cardColors(
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text(task.name)
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                Text(task.description)
            }
        }
    }
}
