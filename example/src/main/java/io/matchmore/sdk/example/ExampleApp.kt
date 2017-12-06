package io.matchmore.sdk.example

import android.support.multidex.MultiDexApplication
import io.matchmore.sdk.MatchMore

class ExampleApp: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MatchMore.config(SdkConfig.apiKey, SdkConfig.worldId)
        MatchMore.instance.startUsingMainDevice(success = { device -> }, error = { it.printStackTrace() })
    }
}