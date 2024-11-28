package com.rago.handlenetwork.presentation.uistate

import java.io.File

data class SignatureUIState(
    val file: File? = null,
    val onAccept: (File) -> Unit = {},
    val setOnAccept: ((File) -> Unit) -> Unit = {},
    val onClear: () -> Unit = {},
)