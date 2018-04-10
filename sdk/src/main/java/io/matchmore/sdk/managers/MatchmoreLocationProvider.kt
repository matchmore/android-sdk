package io.matchmore.sdk.managers

import io.matchmore.sdk.api.models.MatchmoreLocation

interface MatchmoreLocationProvider {

    fun startUpdatingLocation(sender: LocationSender)

    fun stopUpdatingLocation()
}

interface LocationSender {
    fun sendLocation(location: MatchmoreLocation)
}