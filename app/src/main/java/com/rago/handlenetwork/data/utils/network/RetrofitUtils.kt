package com.rago.handlenetwork.data.utils.network

import arrow.core.Either
import com.rago.handlenetwork.data.model.ErrorResponse
import com.rago.handlenetwork.data.model.SuccessResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RetrofitUtils @Inject constructor() {

    private val _uiState: MutableStateFlow<RetrofitUIState> = MutableStateFlow(
        RetrofitUIState(
            onAgainApi = ::onAgainApi,
            cancelCurrentApi = ::cancelCurrentApi
        )
    )
    val uiState: StateFlow<RetrofitUIState> = _uiState.asStateFlow()

    private fun onAgainApi() {
        uiState.value.currentApi?.execute()
        _uiState.update {
            it.copy(showDialogAgain = false)
        }
    }

    private fun cancelCurrentApi() {
        GlobalScope.launch(Dispatchers.IO) {
            uiState.value.currentApi?.cancel()
            _uiState.update {
                it.copy(showDialogAgain = true)
            }
        }
    }

    fun <T> arrow(api: Call<T>): Flow<Either<SuccessResponse<T>, ErrorResponse>> = callbackFlow {
        _uiState.update {
            it.copy(loading = true)
        }
        val callback: Callback<T> = object : Callback<T> {
            override fun onResponse(p0: Call<T>, response: Response<T>) {
                try {
                    if (response.body() != null) {
                        trySend(
                            Either.Left(
                                SuccessResponse(
                                    code = response.code(),
                                    data = response.body()
                                )
                            )
                        )
                        _uiState.update {
                            it.copy(loading = false)
                        }
                    } else {
                        trySend(Either.Left(SuccessResponse(code = response.code())))
                        _uiState.update {
                            it.copy(loading = false)
                        }
                    }
                } catch (e: Exception) {
                    trySend(Either.Right(ErrorResponse(code = 500, error = e.message)))
                    _uiState.update {
                        it.copy(loading = false)
                    }
                }
            }

            override fun onFailure(p0: Call<T>, p1: Throwable) {
                trySend(Either.Right(ErrorResponse(code = 500, error = p1.message)))
                _uiState.update {
                    it.copy(loading = false)
                }
            }

        }

        api.enqueue(callback)

        _uiState.update {
            it.copy(currentApi = api)
        }
        awaitClose {
            if (api.isCanceled) {
                api.cancel()
            }
            _uiState.update {
                it.copy(loading = false)
            }
        }

    }
}