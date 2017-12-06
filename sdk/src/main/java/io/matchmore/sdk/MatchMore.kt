package io.matchmore.sdk

import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback

interface MatchMore {

    fun startUsingMainDevice(device: MobileDevice? = null, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?)

    companion object {

        private var apiKey: String? = null
        private var worldId: String? = null

        val instance: MatchMore by lazy {
            if (this.apiKey == null) throw IllegalStateException("Please config first.")
            AlpsManager( apiKey!!, worldId!!)
        }

        fun config(apiKey: String, worldId: String) {
            if (this.apiKey != null) throw IllegalStateException("You can not overwrite the configuration.")
            this.apiKey = apiKey
            this.worldId = worldId
        }
    }
}