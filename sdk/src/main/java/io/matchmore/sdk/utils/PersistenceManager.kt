package io.matchmore.sdk.utils

import android.content.Context
import com.google.gson.Gson
import okio.Okio
import java.io.File
import java.lang.reflect.Type

class PersistenceManager(context: Context, private val gson: Gson) {

    private val dataDir = context.applicationInfo.dataDir

    fun writeData(data: Any?, fileName: String) {
        val sink = Okio.buffer(Okio.sink(File(dataDir, fileName)))
        sink.writeUtf8(if (data != null) gson.toJson(data) else "")
        sink.close()
    }

    fun <T> readData(fileName: String, type: Type): T? {
        val file = File(dataDir, fileName)
        if (!file.exists()) return null
        val sink = Okio.buffer(Okio.source(file))
        val data = sink.readUtf8()
        sink.close()
        return gson.fromJson(data, type)
    }
}