package io.matchmore.sdk.store

import com.google.gson.reflect.TypeToken
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.MobileDevice

class DeviceStore(private val persistenceManager: PersistenceManager) : CRUD<Device> {

    private var items = listOf<Device>()
        set(value) {
            Thread({ persistenceManager.writeData(value, kMobileDevicesFile) }).start()
            field = value
        }

    var main: MobileDevice? = null
        set(value) {
            Thread({ persistenceManager.writeData(value, kMainDeviceFile) }).start()
            field = value
        }

    init {
        this.main = persistenceManager.readData(kMainDeviceFile, MobileDevice::class.java)
        this.items = persistenceManager.readData(kMobileDevicesFile, object : TypeToken<List<Device>>() {}.type) ?: listOf()
    }

    override fun create(item: Device) {
        items += item
    }

    override fun find(byId: String) = items.firstOrNull { it.id == byId }

    override fun findAll(success: SuccessCallback<List<Device>>, error: ErrorCallback?) {
        TODO("not implemented")
    }

    override fun update(item: Device, success: SuccessCallback<List<Device>>?, error: ErrorCallback?) {
        TODO("not implemented")
    }

    override fun delete(item: Device, error: ErrorCallback?) {
        TODO("not implemented")
    }

    override fun deleteAll(error: ErrorCallback?) {
        TODO("not implemented")
    }

    companion object {
        val kMainDeviceFile = "kMainDeviceFile.Alps"
        val kMobileDevicesFile = "kMobileDevicesFile.Alps"
    }
}