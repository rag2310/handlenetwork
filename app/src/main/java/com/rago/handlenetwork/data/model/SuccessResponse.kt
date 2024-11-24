package com.rago.handlenetwork.data.model

data class SuccessResponse<T>(
    val code: Int = 0,
    val data: T? = null
)