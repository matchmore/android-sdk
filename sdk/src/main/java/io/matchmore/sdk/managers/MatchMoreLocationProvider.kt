package io.matchmore.sdk.managers

import io.matchmore.sdk.api.models.MatchMoreLocation

interface MatchMoreLocationProvider {

    fun startUpdatingLocation(sender: LocationSender)

    fun stopUpdatingLocation()
}

interface LocationSender {
    fun sendLocation(location: MatchMoreLocation)
}