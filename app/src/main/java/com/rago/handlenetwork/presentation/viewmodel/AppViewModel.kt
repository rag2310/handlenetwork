package com.rago.handlenetwork.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rago.handlenetwork.data.utils.network.RetrofitUtils
import com.rago.handlenetwork.presentation.uistate.AppUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState: MutableStateFlow<AppUIState> = MutableStateFlow(
        AppUIState(
            onShowLoading = ::onShowLoading,
            onHideLoading = ::onHideLoading,
        )
    )
    val uiState: StateFlow<AppUIState> = _uiState.asStateFlow()

    private fun onShowLoading() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loading = true)
            }
        }
    }

    private fun onHideLoading() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loading = false)
            }
        }
    }
}