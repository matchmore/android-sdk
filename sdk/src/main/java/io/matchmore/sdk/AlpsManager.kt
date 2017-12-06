package io.matchmore.sdk

import io.matchmore.sdk.api.ApiClient
import io.matchmore.sdk.api.enqueue
import io.matchmore.sdk.api.models.IBeaconDevice
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.api.models.PinDevice
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback

class AlpsManager(apiKey: String, worldId: String) : MatchMore {

    private val apiClient = ApiClient(apiKey)

    init {
        MobileDevice(platform = "", deviceToken = "")
        PinDevice()
        IBeaconDevice()
    }

    override fun startUsingMainDevice(device: MobileDevice?, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?) {
        var finalDevice = device
        if (finalDevice == null) {
            finalDevice = MobileDevice(name = android.os.Build.MODEL, platform = "Android")
        }
        apiClient.deviceApi.createDevice(finalDevice).enqueue({success?.invoke(it as MobileDevice) }, error)
    }
}