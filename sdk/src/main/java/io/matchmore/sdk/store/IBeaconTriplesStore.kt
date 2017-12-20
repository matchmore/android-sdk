package io.matchmore.sdk.store

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.IBeaconTriple

class IBeaconTriplesStore(private val manager: AlpsManager)
    : Store<IBeaconTriple>(manager.persistenceManager, IBEACON_TRIPLES_FILE) {

    fun updateBeaconTriplets(complete: CompleteCallback) {
        manager.apiClient.deviceApi.getIBeaconTriples().async({ beacons ->
            items = beacons
            complete.invoke()
        }, { complete.invoke() })
    }

    companion object {
        private const val IBEACON_TRIPLES_FILE = "kIBeaconTriplesFile.Alps"
    }
}