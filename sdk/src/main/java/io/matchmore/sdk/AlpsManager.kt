package io.matchmore.sdk

import io.matchmore.sdk.api.ApiClient
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.adapters.ParserBuilder
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import io.matchmore.sdk.managers.DeviceManager
import io.matchmore.sdk.managers.PublicationManager
import io.matchmore.sdk.managers.SubscriptionManager
import io.matchmore.sdk.store.DeviceStore
import io.matchmore.sdk.store.PersistenceManager
import io.matchmore.sdk.store.Store

class AlpsManager(matchMoreConfig: MatchMoreConfig) : MatchMoreSdk {
    private val gson = ParserBuilder.gsonBuilder.create()
    private val persistenceManager = PersistenceManager(matchMoreConfig.context, gson)
    private val deviceManager by lazy { DeviceManager(this) }
    private val publicationManager by lazy { PublicationManager(this) }
    private val subscriptionManager by lazy { SubscriptionManager(this) }
    val deviceStore = DeviceStore(persistenceManager)
    val publicationStore = Store<Publication>(persistenceManager, PUBLICATIONS_FILE)
    val subscriptionStore = Store<Subscription>(persistenceManager, SUBSCRIPTIONS_FILE)
    val apiClient = ApiClient(gson, matchMoreConfig)

    override fun startUsingMainDevice(device: MobileDevice?, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?)
            = deviceManager.startUsingMainDevice(device, success, error)

    override fun createPublication(publication: Publication, deviceWithId: String?, success: SuccessCallback<Publication>?, error: ErrorCallback?)
            = publicationManager.createPublication(publication, deviceWithId, success, error)

    override fun createSubscription(subscription: Subscription, deviceWithId: String?, success: SuccessCallback<Subscription>?, error: ErrorCallback?)
            = subscriptionManager.createSubscription(subscription, deviceWithId, success, error)

    companion object {
        private const val PUBLICATIONS_FILE = "kPublicationsFile.Alps"
        private const val SUBSCRIPTIONS_FILE = "kSubscriptionsFile.Alps"
    }
}