package io.matchmore.sdk.utils

import android.content.Context
import com.google.gson.Gson
import okio.Okio
import java.io.File

class PersistenceManager(context: Context, val gson: Gson) {

    val dataDir = context.applicationInfo.dataDir

    fun writeData(data: Any?, fileName: String) {
        val sink = Okio.buffer(Okio.sink(File(dataDir, fileName)))
        sink.writeUtf8(if (data != null) gson.toJson(data) else "")
        sink.close()
    }

    fun getData(fileName: String): String? {
        val file = File(dataDir, fileName)
        if (!file.exists()) return null
        Okio.buffer(Okio.source(file)).use {
            return it.readUtf8()
        }
    }

    inline fun <reified T> readData(fileName: String): T? {
        val data = getData(fileName)
        return if (data == null) null else gson.fromJson<T>(data)
    }
}