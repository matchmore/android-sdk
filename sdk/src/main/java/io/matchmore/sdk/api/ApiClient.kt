package io.matchmore.sdk.api

import com.google.gson.GsonBuilder
import io.matchmore.sdk.api.adapters.DateTimeTypeAdapter
import io.matchmore.sdk.api.adapters.DeviceTypeAdapter
import io.matchmore.sdk.api.adapters.LocalDateTypeAdapter
import io.matchmore.sdk.api.models.Device
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.Executors

class ApiClient(private val apiKey: String, callbackInUIThread: Boolean, debugLog: Boolean) {

    private val retrofit: Retrofit

    init {
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(DateTime::class.java, DateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
                .registerTypeAdapter(Device::class.java, DeviceTypeAdapter())
                .create()

        val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor {
                    it.proceed(it.request().newBuilder().addHeader("api-key", apiKey).build())
                }
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl("$prefix$baseUrl$apiVersion/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
        if (!callbackInUIThread) {
            retrofitBuilder.callbackExecutor(Executors.newSingleThreadExecutor())
        }
        if (debugLog) {
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
        private const val prefix = "https://"
        private const val baseUrl = "api.matchmore.io"
        private const val apiVersion = "/v5"
    }
}