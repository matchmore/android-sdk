package io.matchmore.sdk

import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.MobileDevice

interface MatchMore {

    fun startUsingMainDevice(device: MobileDevice? = null, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?)

    companion object {

        private var apiKey: String? = null
        private var worldId: String? = null

        val instance: MatchMore by lazy {
            if (!isConfigured()) throw IllegalStateException("Please config first.")
            AlpsManager( apiKey!!, worldId!!)
        }

        fun config(apiKey: String, worldId: String) {
            if (isConfigured()) throw IllegalStateException("You can not overwrite the configuration.")
            this.apiKey = apiKey
            this.worldId = worldId
        }

        fun isConfigured() = this.apiKey != null
    }
}