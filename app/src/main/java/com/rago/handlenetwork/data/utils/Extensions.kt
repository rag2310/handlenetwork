package com.rago.handlenetwork.data.utils

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import com.google.gson.Gson
import java.io.File

fun File.toJson(): String? {
    return Uri.encode(Gson().toJson(this))
}

fun SavedStateHandle.getPath(): String? {
    val data = this.get<String>("path")
    return data
}

fun <T> NavHostController.setObj(key: String, data: T) {
    this.currentBackStackEntry?.savedStateHandle?.set(
        key, data
    )
}