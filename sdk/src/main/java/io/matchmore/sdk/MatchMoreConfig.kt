package io.matchmore.sdk

import android.content.Context
import android.util.Base64
import org.json.JSONObject
import java.nio.charset.Charset

data class MatchMoreConfig @JvmOverloads constructor(
        var context: Context,
        val apiKey: String,
        val serverProtocol: String? = null,
        val serverUrl: String? = null,
        val callbackInUIThread: Boolean = true,
        val debugLog: Boolean = false) {
    init {
        context = context.applicationContext
    }

    val worldId: String by lazy {
        val segments = apiKey.split(".")
        if (segments.size < 2) throw IllegalArgumentException("Invalid API Key.")
        val json = Base64.decode(segments[1], Base64.DEFAULT).toString(Charset.defaultCharset())
        return@lazy JSONObject(json).getString("sub")
    }

}