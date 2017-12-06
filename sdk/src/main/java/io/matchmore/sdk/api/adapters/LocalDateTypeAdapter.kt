package io.matchmore.sdk.api.adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.joda.time.LocalDate
import org.joda.time.format.ISODateTimeFormat
import org.json.JSONObject.NULL
import java.io.IOException

internal class LocalDateTypeAdapter : TypeAdapter<LocalDate>() {

    private val formatter = ISODateTimeFormat.date()

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, date: LocalDate?) {
        if (date == null) {
            writer.nullValue()
        } else {
            writer.value(formatter.print(date))
        }
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): LocalDate? {
        when (reader.peek()) {
            NULL -> {
                reader.nextNull()
                return null
            }
            else -> {
                val date = reader.nextString()
                return formatter.parseLocalDate(date)
            }
        }
    }
}