package io.matchmore.sdk.monitoring

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.Match
import java.util.*

interface MatchMonitorListener {
    fun onReceiveMatches(matches: Set<Match>, forDevice: Device)
}

class MatchMonitor(private val manager: AlpsManager) {
    var monitoredDevices = mutableSetOf<Device>()
    var deliveredMatches = mutableSetOf<Match>()

    private var listeners = mutableSetOf<MatchMonitorListener>()

    fun addOnMatchListener(listener: MatchMonitorListener) {
        listeners.add(listener)
    }

    fun removeOnMatchListener(listener: MatchMonitorListener) {
        listeners.remove(listener)
    }

    private var timer: Timer? = null

    fun startPollingMatchesA() {
        if (timer != null) { return }
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getMatches()
            }
        }, 0, 5000) // repeat every 5 sec.
    }

    fun stopPollingMatches() {
        timer?.cancel()
        timer = null
    }

    private fun getMatches() = monitoredDevices.forEach { getMatchesForDevice(it) }

    private fun getMatchesForDevice(device: Device) {
        device.id?.let {
            manager.apiClient.matchesApi.getMatches(it).async({ matches ->
                val newMatches = matches - deliveredMatches
                if (newMatches.isNotEmpty()) {
                    deliveredMatches.addAll(newMatches)
                    listeners.forEach {
                        it.onReceiveMatches(newMatches.toSet(), device)
                    }
                }
            }, null)
        }
    }
}
