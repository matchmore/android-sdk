package io.matchmore.sdk.monitoring

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.Match
import java.util.*

interface MatchMonitorDelegate {
    fun didReceiveMatches(matches: Set<Match>, forDevice: Device)
}

class MatchMonitor(private val manager: AlpsManager, val delegate: MatchMonitorDelegate) {
    var monitoredDevices = mutableSetOf<Device>()
    var deliveredMatches = mutableSetOf<Match>()

    var timer: Timer? = null

    fun startPollingMatchesA() {
        val period: Long = 5000 // repeat every 10 sec.
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getMatches()
            }
        }, 0, period)
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
                if (!(deliveredMatches == union)) {
                    deliveredMatches = union.toMutableSet()
                    delegate?.didReceiveMatches(union, device)
                }
            }, null)
        }
    }
}
