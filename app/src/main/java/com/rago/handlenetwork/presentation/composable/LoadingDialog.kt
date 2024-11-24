package com.rago.handlenetwork.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingDialog(
    showDialog: Boolean = false
) {
    if (showDialog) {
        LoadingDialogContent()
    }
}

@Composable
@Preview(showBackground = true)
private fun LoadingDialogContent() {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.Center,
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
    }
}
