package io.matchmore.sdk.managers

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.MatchmoreLocation
import io.matchmore.sdk.api.models.MobileDevice

class MatchmoreLocationManager(private val manager: AlpsManager) : LocationSender {

    private var locationProvider: MatchmoreLocationProvider? = null

    var lastLocation: MatchmoreLocation? = null

    fun startUpdatingLocation(locationProvider: MatchmoreLocationProvider) {
        this.locationProvider = locationProvider
        locationProvider.startUpdatingLocation(this)
    }

    fun stopUpdatingLocation() {
        locationProvider?.stopUpdatingLocation()
    }

    override fun sendLocation(location: MatchmoreLocation) {
        lastLocation = location
        manager.devices.findAll().filterIsInstance(MobileDevice::class.java).forEach {
            manager.apiClient.locationApi.createLocation(it.id!!, location).async()
        }
    }
}