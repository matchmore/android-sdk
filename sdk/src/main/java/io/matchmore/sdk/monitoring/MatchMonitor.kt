package io.matchmore.sdk.monitoring

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.Match
import java.util.*

interface MatchMonitorDelegate {
    fun didReceiveMatches(matches: Set<Match>, forDevice: Device)
}

class MatchMonitor(private val manager: AlpsManager) {
    var monitoredDevices = mutableSetOf<Device>()
    var deliveredMatches = mutableSetOf<Match>()

    var timer: Timer? = null

    fun startPollingMatchesA() {
        val delay: Long = 0 // delay for 0 sec.
        val period: Long = 10000 // repeat every 10 sec.
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getMatches()
            }
        }, delay, period)
    }

    fun stopPollingMatches() {
        timer?.cancel()
        timer = null
    }

    private fun getMatches() {
        monitoredDevices.forEach {
            getMatchesForDevice(it)
        }
    }

    private fun getMatchesForDevice(device: Device) {
        device.id?.let {
            manager.apiClient.matchesApi.getMatches(it).async({
                val union = deliveredMatches.union(it.toSet())
                if (deliveredMatches.equals(union) == false) {
                    deliveredMatches = union.toMutableSet()
                    // notify delegates / observers
                }
            }, null)
        }
    }
}
