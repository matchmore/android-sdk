package io.matchmore.sdk.store

import android.os.Build
import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.utils.unwrap

typealias DeviceDeleteListener = (String) -> Unit

class DeviceStore(private val manager: AlpsManager)
    : Store<Device>(manager.persistenceManager, MOBILE_DEVICES_FILE), CRD<Device> {

    private var listeners = mutableSetOf<DeviceDeleteListener>()

    fun addOnDeviceDeleteListener(listener: DeviceDeleteListener) {
        listeners.add(listener)
    }

    fun removeOnDeviceDeleteListener(listener: DeviceDeleteListener) {
        listeners.remove(listener)
    }


    var main: MobileDevice? = null
        @Synchronized get() {
            println("GET MAIN_DEVICE: ${field?.id}")
            return field
        }
        @Synchronized set(value) {
            field = value
            println("SET MAIN_DEVICE: ${field?.id}")
            manager.persistenceManager.writeData(value, MAIN_DEVICE_FILE)
        }

    init {
        this.main = manager.persistenceManager.readData<MobileDevice>(MAIN_DEVICE_FILE)
        this.items = manager.persistenceManager.readData<List<Device>>(MOBILE_DEVICES_FILE) ?: arrayListOf()
    }

    fun startUsingMainDevice(device: MobileDevice?, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?) {
        main?.let {
            if (device == null) {
                manager.matchMonitor.startMonitoringFor(it)
                success?.invoke(it)
                return
            }
        }
        val mobileDevice = MobileDevice(
                name = device?.name ?: Build.MODEL,
                platform = device?.platform ?: "Android",
                deviceToken = device?.deviceToken ?: "fcm://${manager.getDeviceToken()}",
                location = device?.location ?: manager.locationManager.lastLocation
        )
        create(mobileDevice, {
            if (it is MobileDevice) {
                if (main == null) main = it
                manager.matchMonitor.startMonitoringFor(it)
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
        item.id?.let { itemId ->
            manager.apiClient.deviceApi.deleteDevice(itemId).async({
                deleteData(item)
                if (item == main) main = null
                manager.matchMonitor.stopMonitoringFor(item)
                listeners.forEach {
                    it.invoke(itemId)
                }
                complete?.invoke()
            }, error)
        } ?: run {
            error(Throwable("Item ID is required"))
        }

    }

    fun registerDeviceToken(token: String) {
        unwrap(main, main?.id, { main, mainId ->
            main.deviceToken = "fcm://$token"
            manager.apiClient.deviceApi.updateDevice(mainId, main).async({ _ -> })
        })
    }

    companion object {
        private const val MOBILE_DEVICES_FILE = "kMobileDevicesFile.Alps"
        private const val MAIN_DEVICE_FILE = "kMainDeviceFile.Alps"
    }
}