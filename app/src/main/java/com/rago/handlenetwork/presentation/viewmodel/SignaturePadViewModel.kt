package com.rago.handlenetwork.presentation.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rago.handlenetwork.presentation.uistate.SignaturePadUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignaturePadViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableStateFlow<SignaturePadUIState> = MutableStateFlow(
        SignaturePadUIState(
            onAddPoint = ::onAddPoint,
            onEndDraw = ::onEndDraw,
            setCanvasSize = ::setCanvasSize,
            onClearPoints = ::onClearPoints
        )
    )

    val uiState: StateFlow<SignaturePadUIState> = _uiState.asStateFlow()

    private fun onAddPoint(offset: Offset) {
        viewModelScope.launch {
            val currentPoints = uiState.value.points.toMutableList()
            currentPoints.add(offset)
            _uiState.update {
                it.copy(
                    points = currentPoints
                )
            }
        }
    }

    private fun onEndDraw() {
        viewModelScope.launch {
            val currentPoints = uiState.value.points
            val currentDraw = uiState.value.draw.toMutableList()
            currentDraw.add(currentPoints)
            _uiState.update {
                it.copy(draw = currentDraw, points = listOf())
            }
        }
    }

    private fun setCanvasSize(size: Size) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(canvasSize = size)
            }
        }
    }

    private fun onClearPoints() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(points = listOf())
            }
        }
    }

}