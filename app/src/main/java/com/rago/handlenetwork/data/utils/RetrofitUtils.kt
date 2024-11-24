package com.rago.handlenetwork.data.utils

import arrow.core.Either
import com.rago.handlenetwork.data.model.ErrorResponse
import com.rago.handlenetwork.data.model.SuccessResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RetrofitUtils @Inject constructor() {
    fun <T> arrow(api: Call<T>): Flow<Either<SuccessResponse<T>, ErrorResponse>> = callbackFlow {
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
                    } else {
                        trySend(Either.Left(SuccessResponse(code = response.code())))
                    }
                } catch (e: Exception) {
                    trySend(Either.Right(ErrorResponse(code = 500, error = e.message)))
                }
            }

            override fun onFailure(p0: Call<T>, p1: Throwable) {
                trySend(Either.Right(ErrorResponse(code = 500, error = p1.message)))
            }

        }

        api.enqueue(callback)

        awaitClose {
            if (api.isCanceled) {
                api.cancel()
            }
        }

    }
}