package io.matchmore.sdk.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.async(success: SuccessCallback<T>? = null, error: ErrorCallback? = null) {
    enqueue(object: Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                success?.invoke(response.body()!!)
            } else {
                error?.invoke(Exception(response.errorBody()!!.string()))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            error?.invoke(t)
        }
    })
}

fun Call<Void>.async(complete: CompleteCallback? = null, error: ErrorCallback? = null) {
    enqueue(object: Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                complete?.invoke()
            } else {
                error?.invoke(Exception(response.errorBody()!!.string()))
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            error?.invoke(t)
        }
    })
}