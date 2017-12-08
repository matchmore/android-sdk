package io.matchmore.sdk.store

import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.MobileDevice

class DeviceStore(private val persistenceManager: PersistenceManager) : Store<Device>(persistenceManager, "kMobileDevicesFile.Alps") {

    var main: MobileDevice? = null
        set(value) {
            Thread({ persistenceManager.writeData(value, kMainDeviceFile) }).start()
            field = value
        }

    init {
        this.main = persistenceManager.readData(kMainDeviceFile, MobileDevice::class.java)
    }

    companion object {
        val kMainDeviceFile = "kMainDeviceFile.Alps"
    }
}