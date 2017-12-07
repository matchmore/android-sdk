package io.matchmore.sdk

import io.matchmore.sdk.api.ApiClient
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.managers.DeviceManager

class AlpsManager(apiKey: String, worldId: String, callbackInUIThread: Boolean, debugLog: Boolean) : MatchMoreSdk {

    private val apiClient = ApiClient(apiKey, callbackInUIThread, debugLog)
    private val deviceManager = DeviceManager(apiClient)

    override fun startUsingMainDevice(device: MobileDevice?, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?)
            = deviceManager.startUsingMainDevice(device, success, error)
}