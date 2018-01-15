package io.matchmore.sdk.store

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.utils.withoutExpired

class PublicationStore(private val manager: AlpsManager) : CRD<Publication>,
        Store<Publication>(manager.persistenceManager, PUBLICATIONS_FILE) {

    override var items: List<Publication> = listOf<Publication>()
        get() = field.withoutExpired()
        set(value) {
            Thread({ manager.persistenceManager.writeData(value, PUBLICATIONS_FILE) }).start()
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
        manager.apiClient.publicationApi.createPublication(item.deviceId!!, item)
                .async({
                    createData(it)
                    success?.invoke(it)
                }, error)
    }

    override fun delete(item: Publication, complete: CompleteCallback?, error: ErrorCallback?) {
        manager.apiClient.publicationApi.deletePublication(item.deviceId!!, item.id!!)
                .async({
                    deleteData(item)
                    complete?.invoke()
                }, error)
    }

    var onDeviceDelete: DeviceDeleteListener = { id ->
        items = items.filter { it.deviceId != id }
    }

    companion object {
        private const val PUBLICATIONS_FILE = "kPublicationsFile.Alps"
    }
}