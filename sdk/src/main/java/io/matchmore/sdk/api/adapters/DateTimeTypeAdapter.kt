package io.matchmore.sdk.api.adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.json.JSONObject.NULL
import java.io.IOException

class DateTimeTypeAdapter : TypeAdapter<DateTime>() {

    private val parseFormatter = ISODateTimeFormat.dateOptionalTimeParser()
    private val printFormatter = ISODateTimeFormat.dateTime()

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, date: DateTime?) {
        if (date == null) {
            writer.nullValue()
        } else {
            writer.value(printFormatter.print(date))
        }
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): DateTime? {
        when (reader.peek()) {
            NULL -> {
                reader.nextNull()
                return null
            }
            else -> {
                val date = reader.nextString()
                return parseFormatter.parseDateTime(date)
            }
        }
    }
}