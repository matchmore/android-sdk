package io.matchmore.sdk.store

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.utils.unwrap
import io.matchmore.sdk.utils.withoutExpired

class PublicationStore(private val manager: AlpsManager) : CRD<Publication>,
        Store<Publication>(manager.persistenceManager, PUBLICATIONS_FILE) {

    override var items = listOf<Publication>()
        get() = field.withoutExpired()
        set(value) {
            Thread { manager.persistenceManager.writeData(value, PUBLICATIONS_FILE) }.start()
            field = value
        }

    init {
        items = manager.persistenceManager.readData<List<Publication>>(PUBLICATIONS_FILE)?.withoutExpired() ?: listOf()
    }

    fun createPublication(publication: Publication, deviceWithId: String? = null, success: SuccessCallback<Publication>?, error: ErrorCallback?) {
        publication.deviceId = deviceWithId ?: publication.deviceId
        create(publication, success, error)
    }

    override fun create(item: Publication, success: SuccessCallback<Publication>?, error: ErrorCallback?) {
        item.deviceId = item.deviceId ?: manager.main?.id
        item.deviceId?.let { deviceId ->
            manager.apiClient.publicationApi.createPublication(deviceId, item)
                    .async({
                        createData(it)
                        success?.invoke(it)
                    }, error)
        } ?: run {
            error(Throwable("Publication has to have device ID."))
        }
    }

    override fun delete(item: Publication, complete: CompleteCallback?, error: ErrorCallback?) {
        unwrap(item.deviceId, item.id, { deviceId, id ->
            manager.apiClient.publicationApi.deletePublication(deviceId, id)
                    .async({
                        deleteData(item)
                        complete?.invoke()
                    }, error)
        }, {
            error(Throwable("Publication has to have ID and device ID."))
        })

    }

    var onDeviceDelete: DeviceDeleteListener = { id ->
        items = items.filter { it.deviceId != id }
    }

    companion object {
        private const val PUBLICATIONS_FILE = "kPublicationsFile.Alps"
    }
}