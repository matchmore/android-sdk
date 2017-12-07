package io.matchmore.sdk.example

import android.support.multidex.MultiDexApplication
import android.util.Log
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.MatchMoreConfig

class ExampleApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MatchMore.config(MatchMoreConfig(this, SdkConfig.apiKey, SdkConfig.worldId))
        MatchMore.instance.startUsingMainDevice(
                { device -> Log.i("MatchMore", "start ${device.name}") },
                { it.printStackTrace() })
    }
}