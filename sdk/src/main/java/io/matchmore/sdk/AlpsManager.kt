package io.matchmore.sdk

import com.google.firebase.iid.FirebaseInstanceId
import io.matchmore.sdk.api.ApiClient
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.adapters.ParserBuilder
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.api.models.PinDevice
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import io.matchmore.sdk.managers.MatchMoreLocationManager
import io.matchmore.sdk.monitoring.MatchMonitor
import io.matchmore.sdk.store.DeviceStore
import io.matchmore.sdk.store.PublicationStore
import io.matchmore.sdk.store.SubscriptionStore
import io.matchmore.sdk.utils.PersistenceManager

class AlpsManager(matchMoreConfig: MatchMoreConfig) : MatchMoreSdk {
    private val gson = ParserBuilder.gsonBuilder.create()
    private val deviceStore by lazy { DeviceStore(this) }
    private val publicationStore by lazy { PublicationStore(this) }
    private val subscriptionStore by lazy { SubscriptionStore(this) }

    val apiClient = ApiClient(gson, matchMoreConfig)
    val persistenceManager = PersistenceManager(matchMoreConfig.context, gson)

    override val main: MobileDevice?
        get() = deviceStore.main

    override fun startUsingMainDevice(device: MobileDevice?, success: SuccessCallback<MobileDevice>?, error: ErrorCallback?)
            = deviceStore.startUsingMainDevice(device, success, error)

    override fun createPublication(publication: Publication, deviceWithId: String?, success: SuccessCallback<Publication>?, error: ErrorCallback?)
            = publicationStore.createPublication(publication, deviceWithId, success, error)

    override fun createSubscription(subscription: Subscription, deviceWithId: String?, success: SuccessCallback<Subscription>?, error: ErrorCallback?)
            = subscriptionStore.createSubscription(subscription, deviceWithId, success, error)

    override  fun createPinDevice(pinDevice: PinDevice, success: SuccessCallback<PinDevice>?, error: ErrorCallback?) = deviceStore.create(pinDevice, {
        success?.invoke(it as PinDevice)
    }, error)

    override val publications = publicationStore

    override val subscriptions = subscriptionStore

    override val devices = deviceStore

    override val matchMonitor = MatchMonitor(this)

    override val locationManager = MatchMoreLocationManager(matchMoreConfig.context, this)

    override fun startUpdatingLocation() = locationManager.startUpdatingLocation()

    override fun stopUpdatingLocation() = locationManager.stopUpdatingLocation()

    fun registerDeviceToken(token: String) {

    }

    fun processPushNotification(data: Map<String, String>) {
        data["matchId"]?.let { matchMonitor.onReceiveMatchUpdate(it) }
    }

    fun getDeviceToken() =
            try {
                FirebaseInstanceId.getInstance().token ?: ""
            } catch (ex: IllegalStateException) {
                ""
            }
}