package com.rago.handlenetwork.data.utils.network

import retrofit2.Call

data class RetrofitUIState(
    val loading: Boolean = false,
    val currentApi: Call<*>? = null,
    val showDialogAgain: Boolean = false,
    val onAgainApi: () -> Unit = {},
    val cancelCurrentApi: () -> Unit = {}
)