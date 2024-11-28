package com.rago.handlenetwork.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rago.handlenetwork.presentation.uistate.SignatureUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignatureViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<SignatureUIState> = MutableStateFlow(
        SignatureUIState(
            setOnNavSignaturePad = ::setOnNavSignaturePad,
            setPath = ::setFile,
        )
    )
    val uiState: StateFlow<SignatureUIState> = _uiState.asStateFlow()


    private fun setOnNavSignaturePad(onNavSignaturePad: () -> Unit) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(onNavSignaturePad = onNavSignaturePad)
            }
        }
    }

    private fun setFile(path: String?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(path = path)
            }
        }
    }
}