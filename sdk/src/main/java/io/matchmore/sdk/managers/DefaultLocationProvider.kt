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
import io.matchmore.sdk.utils.mmLocation


class DefaultLocationProvider(private val context: Context): MatchmoreLocationProvider {

    private var sender: LocationSender? = null

    private val listener: LocationListener = object : LocationListener {
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {}

        override fun onLocationChanged(location: Location) {
            sender?.sendLocation(location.mmLocation)
        }
    }

    private val locationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun findProvider() = locationManager.getBestProvider(Criteria().apply {
        accuracy = Criteria.ACCURACY_FINE
    }, false)

    override fun startUpdatingLocation(sender: LocationSender) {
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val provider = findProvider()
                    ?: throw IllegalStateException("Can't find location provider.")
            val location = locationManager.getLastKnownLocation(provider)
            if (location != null) sender.sendLocation(location.mmLocation)
            locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, listener)
        } else {
            throw SecurityException("You need to get ACCESS_FINE_LOCATION permission first. ")
        }
    }

    override fun stopUpdatingLocation() {
        locationManager.removeUpdates(listener)
    }

    companion object {
        private const val MIN_TIME = 10 * 1000L //10s
        private const val MIN_DISTANCE = 10f //10m
    }
}