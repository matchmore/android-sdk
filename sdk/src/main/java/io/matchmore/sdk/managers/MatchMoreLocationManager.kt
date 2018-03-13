package io.matchmore.sdk.managers

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.MatchMoreLocation
import io.matchmore.sdk.api.models.MobileDevice

class MatchMoreLocationManager(private val manager: AlpsManager) : LocationSender {

    private var locationProvider: MatchMoreLocationProvider? = null

    var lastLocation: MatchMoreLocation? = null

    fun startUpdatingLocation(locationProvider: MatchMoreLocationProvider) {
        this.locationProvider = locationProvider
        locationProvider.startUpdatingLocation(this)
    }

    fun stopUpdatingLocation() {
        locationProvider?.stopUpdatingLocation()
    }

    override fun sendLocation(location: MatchMoreLocation) {
        lastLocation = location
        manager.devices.findAll().filterIsInstance(MobileDevice::class.java).forEach {
            manager.apiClient.locationApi.createLocation(it.id!!, location).async()
        }
    }
}