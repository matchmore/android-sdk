package io.matchmore.sdk.utils

import com.google.gson.Gson
import io.matchmore.sdk.MatchmoreConfig
import okio.Okio
import java.io.File

class PersistenceManager(config: MatchmoreConfig, val gson: Gson) {

    private val dataDir = File(config.context.applicationInfo.dataDir, "mm/${config.worldId}")

    init {
        dataDir.mkdirs()
    }

    fun writeData(data: Any?, fileName: String) {
        val sink = Okio.buffer(Okio.sink(File(dataDir, fileName)))
        sink.writeUtf8(if (data != null) gson.toJson(data) else "")
        sink.close()
    }

    @Suppress("MemberVisibilityCanBePrivate")
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