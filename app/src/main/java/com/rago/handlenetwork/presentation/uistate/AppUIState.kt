package com.rago.handlenetwork.presentation.uistate

data class AppUIState(
    val loading: Boolean = false,
    val onShowLoading: () -> Unit = {},
    val onHideLoading: () -> Unit = {}
)