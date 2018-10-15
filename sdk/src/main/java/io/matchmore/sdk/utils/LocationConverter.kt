package io.matchmore.sdk.utils

import android.location.Location
import io.matchmore.sdk.api.models.MatchmoreLocation

val Location.mmLocation: MatchmoreLocation
    get() {
        return MatchmoreLocation(
                latitude = this.latitude,
                longitude = this.longitude
        )
    }
