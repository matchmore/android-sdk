package io.matchmore.sdk.monitoring

import okhttp3.WebSocket
import okhttp3.WebSocketListener


typealias OnMessage = (String?) -> Unit
typealias OnClosed = (Int, String?) -> Unit


class MatchSocketListener : WebSocketListener() {

    val onMessage: OnMessage? = null
    val onClosed: OnClosed? = null

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        onMessage?.invoke(text)
    }

    override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
        onClosed?.invoke(code, reason)
    }
}