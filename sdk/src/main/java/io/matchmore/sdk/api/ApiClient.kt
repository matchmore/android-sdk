package io.matchmore.sdk.api

import com.google.gson.Gson
import io.matchmore.sdk.MatchMoreConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.Executors

class ApiClient(gson: Gson, private val matchMoreConfig: MatchMoreConfig) {

    private val retrofit: Retrofit

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor {
                    it.proceed(it.request().newBuilder().addHeader("api-key", matchMoreConfig.apiKey).build())
                }
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl("$prefix$baseUrl$apiVersion/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
        if (!matchMoreConfig.callbackInUIThread) {
            retrofitBuilder.callbackExecutor(Executors.newSingleThreadExecutor())
        }
        if (matchMoreConfig.debugLog) {
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        retrofit = retrofitBuilder.client(okHttpClientBuilder.build()).build()
    }

    val deviceApi by lazy { retrofit.create(DeviceApi::class.java) }
    val locationApi by lazy { retrofit.create(LocationApi::class.java) }
    val matchesApi by lazy { retrofit.create(MatchesApi::class.java) }
    val publicationApi by lazy { retrofit.create(PublicationApi::class.java) }
    val subscriptionApi by lazy { retrofit.create(SubscriptionApi::class.java) }

    companion object {
//        private const val prefix = "https://"
//        private const val baseUrl = "api.matchmore.io"
        private const val prefix = "http://"
        private const val baseUrl = "35.201.116.232"
        private const val apiVersion = "/v5"
    }
}