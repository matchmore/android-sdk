package io.matchmore.sdk.example

import android.support.multidex.MultiDexApplication
import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.MatchMoreConfig

class ExampleApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MatchMore.config(MatchMoreConfig(this, SdkConfigTest.API_KEY, SdkConfigTest.WORLD_ID, debugLog = true))
    }
}