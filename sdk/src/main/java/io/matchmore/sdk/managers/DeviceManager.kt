package io.matchmore.sdk.managers

import android.os.Build
import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.MobileDevice

class DeviceManager(private val manager: AlpsManager) {

    fun startUsingMainDevice(device: MobileDevice?, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?) {
        manager.deviceStore.main?.let {
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
        manager.apiClient.deviceApi.createDevice(mobileDevice).async({
            val mDevice = it as MobileDevice
            manager.deviceStore.create(mDevice)
            if (manager.deviceStore.main == null) manager.deviceStore.main = mDevice
            success?.invoke(mDevice)
        }, error)
    }
}