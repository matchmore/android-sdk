package io.matchmore.sdk

import android.content.Context

data class MatchMoreConfig @JvmOverloads constructor(
        var context: Context,
        val apiKey: String,
        val worldId: String,
        val serverProtocol: String? = null,
        val serverUrl: String? = null,
        val callbackInUIThread: Boolean = true,
        val debugLog: Boolean = false) {
    init {
        context = context.applicationContext
    }
}