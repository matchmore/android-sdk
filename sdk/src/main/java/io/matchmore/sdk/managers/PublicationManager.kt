package io.matchmore.sdk.managers

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Publication

class PublicationManager(private val manager: AlpsManager) {

    fun createPublication(publication: Publication, deviceWithId: String? = null, success: SuccessCallback<Publication>?, error: ErrorCallback?) {
        publication.deviceId = deviceWithId ?: manager.deviceStore.main?.id
        manager.apiClient.publicationApi.createPublication(publication.deviceId!!, publication)
                .async({
                    manager.publicationStore.create(it)
                    success?.invoke(it)
                }, error)
    }
}