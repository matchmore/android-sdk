package io.matchmore.sdk.managers

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.utils.mmLocation


class MatchMoreLocationManager(private val context: Context, private val manager: AlpsManager) {

    var lastLocation: io.matchmore.sdk.api.models.Location? = null

    private var started = false

    private val listener: LocationListener = object : LocationListener {
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {}

        override fun onLocationChanged(location: Location) {
            sendLocation(location.mmLocation)
        }
    }

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private fun findProvider() = locationManager.getBestProvider(Criteria().apply {
        accuracy = Criteria.ACCURACY_FINE
    }, false)

    fun startUpdatingLocation() {
        if (started) return
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val provider = findProvider() ?: throw IllegalStateException("Can't find location provider.")
            val location = locationManager.getLastKnownLocation(provider)
            if (location != null) sendLocation(location.mmLocation)
            locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, listener)
            started = true
        } else {
            throw SecurityException("You need to get ACCESS_FINE_LOCATION permission first. ")
        }
    }

    fun stopUpdatingLocation() {
        locationManager.removeUpdates(listener)
        started = false
    }

    internal fun sendLocation(location: io.matchmore.sdk.api.models.Location, completion: CompleteCallback? = null) {
        manager.devices.findAll().filterIsInstance(MobileDevice::class.java).forEach {
            manager.apiClient.locationApi.createLocation(it.id!!, location).async({ _ -> // API method is broken, it does not return location
                lastLocation = location
                completion?.invoke()
            }, {
                completion?.invoke()
            })
        }
    }

    companion object {
        private const val MIN_TIME = 10 * 1000L //10s
        private const val MIN_DISTANCE = 10f //10m
    }
}