package io.matchmore.sdk

import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.MobileDevice

object MatchMore {

    private var apiKey: String? = null
    private lateinit var worldId: String
    private var callbackInUIThread = true
    private var debugLog = false

    @JvmStatic
    val instance: MatchMoreSdk by lazy {
        if (!isConfigured()) throw IllegalStateException("Please config first.")
        AlpsManager(apiKey!!, worldId, callbackInUIThread, debugLog)
    }

    @JvmStatic
    @JvmOverloads
    fun config(apiKey: String, worldId: String, callbackInUIThread: Boolean = true, debugLog: Boolean = false) {
        if (isConfigured()) throw IllegalStateException("You can not overwrite the configuration.")
        this.apiKey = apiKey
        this.worldId = worldId
        this.callbackInUIThread = callbackInUIThread
        this.debugLog = debugLog
    }

    @JvmStatic
    fun isConfigured() = this.apiKey != null
}

interface MatchMoreSdk {
    fun startUsingMainDevice(success: SuccessCallback<MobileDevice>?, error: ErrorCallback?) = startUsingMainDevice(null, success, error)
    fun startUsingMainDevice(device: MobileDevice? = null, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?)
}