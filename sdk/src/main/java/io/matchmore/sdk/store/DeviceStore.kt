package io.matchmore.sdk.store

import android.os.Build
import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.MobileDevice

class DeviceStore(private val manager: AlpsManager)
    : Store<Device>(manager.persistenceManager, MOBILE_DEVICES_FILE), CRD<Device> {

    var main: MobileDevice? = null
        set(value) {
            manager.persistenceManager.writeData(value, MAIN_DEVICE_FILE)
            field = value
        }

    init {
        this.main = manager.persistenceManager.readData(MAIN_DEVICE_FILE, MobileDevice::class.java)
    }

    fun startUsingMainDevice(device: MobileDevice?, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?) {
        main?.let {
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
        create(mobileDevice, {
            if (it is MobileDevice) {
                if (main == null) main = it
                success?.invoke(it)
            }
        }, error)
    }

    override fun create(item: Device, success: SuccessCallback<Device>?, error: ErrorCallback?) {
        manager.apiClient.deviceApi.createDevice(item).async({ device ->
            createData(device)
            success?.invoke(device)
        }, error)
    }

    override fun delete(item: Device, complete: CompleteCallback?, error: ErrorCallback?) {
        manager.apiClient.deviceApi.deleteDevice(item.id!!).async({
            deleteData(item)
            if (item == main) main = null
            complete?.invoke()
        }, error)
    }

    companion object {
        val MOBILE_DEVICES_FILE = "kMobileDevicesFile.Alps"
        val MAIN_DEVICE_FILE = "kMainDeviceFile.Alps"
    }
}