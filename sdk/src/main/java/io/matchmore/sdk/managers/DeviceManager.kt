package io.matchmore.sdk.managers

import android.os.Build
import io.matchmore.sdk.api.ApiClient
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.store.DeviceStore

class DeviceManager(private val apiClient: ApiClient, private val deviceStore: DeviceStore) {

    fun startUsingMainDevice(device: MobileDevice?, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?) {
        deviceStore.main?.let {
            if (device == null) {
//                TODO manager.matchMonitor.startMonitoringFor(device: mainDevice)
                success?.invoke(it)
                return
            }
        }
        val mobileDevice = MobileDevice(
                name = device?.name ?: Build.MODEL,
                platform = device?.platform ?: "Android",
                deviceToken = device?.deviceToken ?: "",
                location = device?.location //TODO ?: lastLocation
        )
        apiClient.deviceApi.createDevice(mobileDevice).async({
            val mDevice = it as MobileDevice
            deviceStore.create(mDevice)
            if (deviceStore.main == null) deviceStore.main = mDevice
            success?.invoke(mDevice)
        }, error)
    }
}