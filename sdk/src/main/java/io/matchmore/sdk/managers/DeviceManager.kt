package io.matchmore.sdk.managers

import android.os.Build
import io.matchmore.sdk.api.ApiClient
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.MobileDevice

class DeviceManager(private val apiClient: ApiClient) {

    fun startUsingMainDevice(device: MobileDevice?, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?) {
        var finalDevice = device
        if (finalDevice == null) {
            finalDevice = MobileDevice(name = Build.MODEL, platform = "Android")
        }
        apiClient.deviceApi.createDevice(finalDevice).async({success?.invoke(it as MobileDevice) }, error)
    }
}