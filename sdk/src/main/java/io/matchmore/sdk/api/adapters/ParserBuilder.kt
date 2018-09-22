package io.matchmore.sdk.api.adapters

import com.google.gson.GsonBuilder
import io.matchmore.sdk.api.models.Device
import org.joda.time.DateTime
import org.joda.time.LocalDate

object ParserBuilder {
    val gsonBuilder: GsonBuilder = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .registerTypeAdapter(DateTime::class.java, DateTimeTypeAdapter())
            .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
            .registerTypeAdapter(Device::class.java, DeviceTypeAdapter())
}