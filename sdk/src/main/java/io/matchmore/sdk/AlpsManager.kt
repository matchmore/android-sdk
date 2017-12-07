package io.matchmore.sdk

import io.matchmore.sdk.api.ApiClient
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.adapters.ParserBuilder
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.managers.DeviceManager
import io.matchmore.sdk.store.DeviceStore
import io.matchmore.sdk.store.PersistenceManager

class AlpsManager(matchMoreConfig: MatchMoreConfig) : MatchMoreSdk {
    private val gson = ParserBuilder.gsonBuilder.create()
    private val apiClient = ApiClient(gson, matchMoreConfig)
    private val deviceManager by lazy { DeviceManager(apiClient, deviceStore) }
    private val persistenceManager = PersistenceManager(matchMoreConfig.context, gson)
    private val deviceStore = DeviceStore(persistenceManager)

    override fun startUsingMainDevice(device: MobileDevice?, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?)
            = deviceManager.startUsingMainDevice(device, success, error)
}