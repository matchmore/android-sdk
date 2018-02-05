package io.matchmore.sdk.utils

import android.location.Location
import android.os.Build

val Location.mmLocation: io.matchmore.sdk.api.models.MatchMoreLocation
    get() {
        var apiLocation = io.matchmore.sdk.api.models.MatchMoreLocation(
                latitude = this.latitude,
                longitude = this.longitude,
                horizontalAccuracy = if (this.hasAccuracy()) this.accuracy.toDouble() else 1.0
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            apiLocation = apiLocation.copy(
                    verticalAccuracy = if (this.hasVerticalAccuracy()) {
                        this.verticalAccuracyMeters.toDouble()
                    } else {
                        1.0
                    }
            )
        }
        return apiLocation
    }
