package io.matchmore.sdk.managers

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.util.Log
import io.matchmore.sdk.api.ApiClient
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.ProximityEvent
import io.matchmore.sdk.store.DeviceStore
import org.altbeacon.beacon.*

class MatchMoreBeaconManager(
        private val context: Context,
        private val apiClient: ApiClient,
        private val deviceStore: DeviceStore
) : BeaconConsumer {

    private val beaconsTriggered = mutableMapOf<Identifier, TriggerInfo>()

    private val beaconManager = BeaconManager.getInstanceForApplication(context).apply {
        beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
    }

    fun startRanging() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled) {
                bluetoothAdapter.enable()
            }
            beaconManager.bind(this)
        }
    }

    fun stopRanging() {
        beaconManager.unbind(this)
    }

    override fun onBeaconServiceConnect() {
        beaconManager.addRangeNotifier { beacons, _ ->
            beacons.forEach { beacon ->
                Log.i("beacon", "${beacon.id1} ${beacon.distance}")
                if (shouldTrigger(beacon)) {
                    val proximityEvent = ProximityEvent(deviceId = beacon.id1.toString(), distance = beacon.distance)
                    apiClient.deviceApi.triggerProximityEvents(deviceStore.main!!.id!!, proximityEvent)
                            .async({ beaconsTriggered.put(beacon.id1, TriggerInfo(System.currentTimeMillis(), beacon.distance)) })
                }
            }
        }
        beaconManager.startRangingBeaconsInRegion(Region(RANGING_ID, null, null, null))
    }

    private fun shouldTrigger(beacon: Beacon): Boolean {
        val triggerInfo = beaconsTriggered[beacon.id1] ?: return true
        return System.currentTimeMillis() - triggerInfo.timestamp > REFRESH_TIMER
                || Math.abs(triggerInfo.distance - beacon.distance) > REFRESH_DISTANCE
    }

    override fun getApplicationContext() = context

    override fun unbindService(conn: ServiceConnection) = context.unbindService(conn)

    override fun bindService(service: Intent, conn: ServiceConnection, flags: Int) = context.bindService(service, conn, flags)

    companion object {
        private const val RANGING_ID = "MatchMore"
        private const val REFRESH_TIMER = 5 * 1000 // timer is in milliseconds
        private const val REFRESH_DISTANCE = 10
    }
}

data class TriggerInfo(val timestamp: Long, val distance: Double)