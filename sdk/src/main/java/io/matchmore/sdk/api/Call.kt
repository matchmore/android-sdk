package io.matchmore.sdk.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.enqueue(success: SuccessCallback<T>?, error: ErrorCallback?) {
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