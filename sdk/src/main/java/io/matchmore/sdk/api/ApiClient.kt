package io.matchmore.sdk.api

import com.google.gson.GsonBuilder
import io.matchmore.sdk.api.adapters.DateTimeTypeAdapter
import io.matchmore.sdk.api.adapters.LocalDateTypeAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.Executors

class ApiClient(var apiKey: String) {

    private val retrofit: Retrofit

    init {
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(DateTime::class.java, DateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
                .create()
        retrofit = Retrofit.Builder()
                .baseUrl("$prefix$baseUrl$apiVersion/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .client(OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor {
                            it.proceed(it.request().newBuilder().addHeader("api-key", apiKey).build())
                        }.build()
                ).build()
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