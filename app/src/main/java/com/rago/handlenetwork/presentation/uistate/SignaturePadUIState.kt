package com.rago.handlenetwork.presentation.uistate

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class SignaturePadUIState(
    val points: List<Offset> = listOf(),
    val draw: List<List<Offset>> = listOf(),
    val onAddPoint: (Offset) -> Unit = {},
    val onEndDraw: () -> Unit = {},
    val onClearPoints: () -> Unit = {},
    val canvasSize: Size? = null,
    val setCanvasSize: (Size) -> Unit = {}
)