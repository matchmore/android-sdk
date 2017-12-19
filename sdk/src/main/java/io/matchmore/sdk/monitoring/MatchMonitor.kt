package io.matchmore.sdk.monitoring

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.Match
import java.util.*

typealias MatchMonitorListener = (Set<Match>, Device) -> Unit

class MatchMonitor(private val manager: AlpsManager) {
    var monitoredDevices = mutableSetOf<Device>()
    var deliveredMatches = mutableSetOf<Match>()

    private var listeners = mutableSetOf<MatchMonitorListener>()

    private var timer: Timer? = null

    fun addOnMatchListener(listener: MatchMonitorListener) {
        listeners.add(listener)
    }

    fun removeOnMatchListener(listener: MatchMonitorListener) {
        listeners.remove(listener)
    }

    fun startMonitoringFor(device: Device) {
        monitoredDevices.add(device)
    }

    fun stopMonitoringFor(device: Device) {
        monitoredDevices.remove(device)
    }

    fun startPollingMatches() {
        if (timer != null) {
            return
        }
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getMatches()
            }
        }, delay, repeat)
    }

    fun stopPollingMatches() {
        timer?.cancel()
        timer = null
    }

    fun onReceiveMatchUpdate(deviceId: String) {
        monitoredDevices.filter { it.id == deviceId }.forEach { getMatchesForDevice(it) }
    }

    private fun getMatches() = monitoredDevices.forEach { getMatchesForDevice(it) }

    private fun getMatchesForDevice(device: Device) {
        device.id?.let {
            manager.apiClient.matchesApi.getMatches(it).async({ matches ->
                val newMatches = matches - deliveredMatches
                if (newMatches.isNotEmpty()) {
                    deliveredMatches.addAll(newMatches)
                    listeners.forEach {
                        it.invoke(newMatches.toSet(), device)
                    }
                }
            }, null)
        }
    }

    companion object {
        private const val delay: Long = 0
        private const val repeat: Long = 5000
    }
}
