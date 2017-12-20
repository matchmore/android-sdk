package io.matchmore.sdk.monitoring

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.MatchMoreConfig
import io.matchmore.sdk.MatchMoreSdk
import io.matchmore.sdk.api.ApiClient
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.Match
import java.util.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

typealias MatchMonitorListener = (Set<Match>, Device) -> Unit

class MatchMonitor(private val manager: AlpsManager, private val config: MatchMoreConfig) {
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

    val listener = MatchSocketListener()
    var socket: WebSocket? = null
    fun openSocketForMatches() {
        if (socket != null) { return }
        val deviceId = MatchMore.instance.devices.findAll().first().id
        val request = Request.Builder().url("ws://$ApiClient.baseUrl/pusher/v5/ws/$deviceId").header("api_key", config.worldId).build()
        val client = OkHttpClient()
        listener.onMessage = { text ->
            if (text != "") {
                getMatches()
            }
        }
        listener.onClosed = { _, _ ->
            socket = client.newWebSocket(request, listener)
            client.dispatcher().executorService().shutdown()
        }
        socket = client.newWebSocket(request, listener)
        client.dispatcher().executorService().shutdown()
    }

    fun closeSocketForMatche() {
        socket?.close(1001,"closed by SDK")
        socket = null
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
