package io.matchmore.sdk

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.RequiresPermission
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.*
import io.matchmore.sdk.managers.MatchmoreLocationManager
import io.matchmore.sdk.managers.MatchmoreLocationProvider
import io.matchmore.sdk.monitoring.MatchMonitor
import io.matchmore.sdk.store.AsyncReadable
import io.matchmore.sdk.store.CRD

@SuppressLint("StaticFieldLeak")
object Matchmore {
    private var matchmoreConfig: MatchmoreConfig? = null

    @JvmStatic
    val instance: MatchmoreSDK by lazy {
        if (!isConfigured()) throw IllegalStateException("Please config first.")
        AlpsManager(matchmoreConfig!!)

    }

    @JvmStatic
    @JvmOverloads
    fun config(context: Context, apiKey: String, debugLog: Boolean = false) {
        if (isConfigured()) throw IllegalStateException("You can not overwrite the configuration.")
        this.matchmoreConfig = MatchmoreConfig(context, apiKey, debugLog)
    }

    @JvmStatic
    fun isConfigured() = this.matchmoreConfig != null
}

interface MatchmoreSDK {
    /**
     * Main mobile device created by `startUsingMainDevice()`.
     */
    val main: MobileDevice?

    /**
     * Async store of all created publications.
     */
    val publications: CRD<Publication>

    /**
     * Async store of all created subscriptions.
     */
    val subscriptions: CRD<Subscription>

    /**
     * Async store of all created devices.
     */
    val devices: CRD<Device>

    /**
     * Async store of all known iBeacon Triples.
     */
    val knownBeacons: AsyncReadable<IBeaconTriple>

    /**
     * [MatchMonitor] allows getting information about new matches, by using listeners.
     * It can be used, by opening socket, polling or remote notification.
     */
    val matchMonitor: MatchMonitor

    /**
     * Object responsible for getting location data that will be sent to Matchmore's cloud.
     * It's possible to inject your own location manager by conforming to [MatchmoreLocationManager] interface.
     */
    val locationManager: MatchmoreLocationManager

    /**
     * Cached set of matches that have been downloaded so far.
     */
    val matches: Set<Match>

    /**
     * starts updating and sending user's location to Matchmore's cloud.
     * Frequency of updates is strictly related to location manager configuration.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun startUpdatingLocation()

    /**
     * Starts updating and sending user's location to Matchmore's cloud.
     * Frequency of updates is strictly related to custom object's configuration.
     * @property locationProvider object that provides location data, it has to conform to [MatchmoreLocationProvider] interface.
     */
    fun startUpdatingLocation(locationProvider: MatchmoreLocationProvider)

    /**
     * Stops updating and sending user's location to Matchmore's cloud.
     */
    fun stopUpdatingLocation()

    /**
     * Starts ranging known beacons. Known beacon id's can be configured in Matchmore's portal (https://matchmore.io)
     */
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH])
    fun startRanging()

    /**
     * Stops ranging known beacons.
     */
    fun stopRanging()

    /**
     * Automatically creates or reuses cached mobile device object. Mobile device object is used to represent the user's smartphone.
     *
     * @property success Callback that returns successful response from the Matchmore's cloud.
     * @property error Callback that returns error response from the Matchmore's cloud.
     */
    fun startUsingMainDevice(success: SuccessCallback<MobileDevice>? = null, error: ErrorCallback? = null)
            = startUsingMainDevice(null, success, error)

    /**
     * Creates given mobile device object. Mobile device object is used to represent the user's smartphone.
     *
     * @property device [MobileDevice] object that will be created on Matchmore's cloud.
     * @property success Callback that returns successful response from the Matchmore's cloud.
     * @property error Callback that returns error response from the Matchmore's cloud.
     */
    fun startUsingMainDevice(device: MobileDevice? = null, success: SuccessCallback<MobileDevice>? = null, error: ErrorCallback? = null)

    /**
     * Creates new publication attached to main device.
     *
     * @property publication [Publication] object that will be created on Matchmore's cloud.
     * @property success Callback that returns successful response from the Matchmore's cloud.
     * @property error Callback that returns error response from the Matchmore's cloud.
     */
    fun createPublicationForMainDevice(publication: Publication, success: SuccessCallback<Publication>?, error: ErrorCallback?)
            = createPublication(publication, null, success, error)

    /**
     * Creates new publication attached to device with given id.
     *
     * @property publication [Publication] object that will be created on Matchmore's cloud.
     * @property deviceWithId id of the device that publication will be attached to.
     * @property success Callback that returns successful response's object from the Matchmore's cloud.
     * @property error Callback that returns error response from the Matchmore's cloud.
     */
    fun createPublication(publication: Publication, deviceWithId: String? = null, success: SuccessCallback<Publication>?, error: ErrorCallback?)

    /**
     * Creates new subscription attached to main device.
     *
     * @property subscription [Subscription] object that will be created on Matchmore's cloud.
     * @property success Callback that returns successful response's object from the Matchmore's cloud.
     * @property error Callback that returns error response from the Matchmore's cloud.
     */
    fun createSubscriptionForMainDevice(subscription: Subscription, success: SuccessCallback<Subscription>?, error: ErrorCallback?)
            = createSubscription(subscription, null, success, error)

    /**
     * Creates new subscription attached to device with given id.
     *
     * @property subscription [Subscription] object that will be created on Matchmore's cloud.
     * @property deviceWithId id of the device that publication will be attached to.
     * @property success Callback that returns successful response's object from the Matchmore's cloud.
     * @property error Callback that returns error response from the Matchmore's cloud.
     */
    fun createSubscription(subscription: Subscription, deviceWithId: String? = null, success: SuccessCallback<Subscription>?, error: ErrorCallback?)

    /**
     * Creates new pin device. Device created this way can be accessed via devices store: devices property or through callback's result.
     *
     * @property pinDevice [PinDevice] object that will be created on Matchmore's cloud.
     * @property success Callback that returns successful response's object from the Matchmore's cloud.
     * @property error Callback that returns error response from the Matchmore's cloud.
     */
    fun createPinDevice(pinDevice: PinDevice, success: SuccessCallback<PinDevice>?, error: ErrorCallback?)
}