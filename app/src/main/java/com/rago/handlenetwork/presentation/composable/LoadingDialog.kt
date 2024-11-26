package com.rago.handlenetwork.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rago.handlenetwork.data.utils.network.RetrofitUIState
import kotlinx.coroutines.delay

@Composable
fun LoadingDialog(
    showDialog: Boolean = false,
    retrofitUIState: RetrofitUIState
) {
    if (retrofitUIState.loading) {
        Dialog(onDismissRequest = {}) {
            LoadingDialogContent(retrofitUIState)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LoadingDialogContent(retrofitUIState: RetrofitUIState = RetrofitUIState()) {
    val showCancelButton = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        delay(5000)
        showCancelButton.value = true
    }
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
                Spacer(Modifier.width(10.dp))
                Text(
                    "Cargando...",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            androidx.compose.animation.AnimatedVisibility(showCancelButton.value) {
                Column {

                    Spacer(Modifier.height(16.dp))
                    Text("mmmm algo esta ocurriendo")
                    TextButton(onClick = {
                        retrofitUIState.cancelCurrentApi()
                    }) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}
