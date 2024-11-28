package com.rago.handlenetwork.presentation.uistate

import java.io.File

data class SignatureUIState(
    val onNavSignaturePad: () -> Unit = {},
    val setOnNavSignaturePad: (() -> Unit) -> Unit = {},
    val setPath: (String?) -> Unit = {},
    val path: String? = null
)