package io.matchmore.sdk

import android.Manifest
import android.annotation.SuppressLint
import android.support.annotation.RequiresPermission
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.*
import io.matchmore.sdk.managers.MatchMoreLocationManager
import io.matchmore.sdk.monitoring.MatchMonitor
import io.matchmore.sdk.store.CRD

@SuppressLint("StaticFieldLeak")
object MatchMore {

    private var matchMoreConfig: MatchMoreConfig? = null

    @JvmStatic
    val instance: MatchMoreSdk by lazy {
        if (!isConfigured()) throw IllegalStateException("Please config first.")
        AlpsManager(matchMoreConfig!!)
    }

    @JvmStatic
    fun config(matchMoreConfig: MatchMoreConfig) {
        if (isConfigured()) throw IllegalStateException("You can not overwrite the configuration.")
        this.matchMoreConfig = matchMoreConfig
    }

    @JvmStatic
    fun isConfigured() = this.matchMoreConfig != null
}

interface MatchMoreSdk {
    val main: MobileDevice?

    val publications: CRD<Publication>

    val subscriptions: CRD<Subscription>

    val devices: CRD<Device>

    val matchMonitor: MatchMonitor

    val locationManager: MatchMoreLocationManager

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun startUpdatingLocation()

    fun stopUpdatingLocation()

    fun startUsingMainDevice(success: SuccessCallback<MobileDevice>? = null, error: ErrorCallback? = null)
            = startUsingMainDevice(null, success, error)

    fun startUsingMainDevice(device: MobileDevice? = null, success: SuccessCallback<MobileDevice>? = null, error: ErrorCallback? = null)

    fun createPublication(publication: Publication, success: SuccessCallback<Publication>?, error: ErrorCallback?)
            = createPublication(publication, null, success, error)

    fun createPublication(publication: Publication, deviceWithId: String? = null, success: SuccessCallback<Publication>?, error: ErrorCallback?)

    fun createSubscription(subscription: Subscription, success: SuccessCallback<Subscription>?, error: ErrorCallback?)
            = createSubscription(subscription, null, success, error)

    fun createSubscription(subscription: Subscription, deviceWithId: String? = null, success: SuccessCallback<Subscription>?, error: ErrorCallback?)

    fun createPinDevice(pinDevice: PinDevice, success: SuccessCallback<PinDevice>?, error: ErrorCallback?)
}