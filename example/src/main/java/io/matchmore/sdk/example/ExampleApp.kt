package io.matchmore.sdk.example

import android.support.multidex.MultiDexApplication
import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.MatchMore

class ExampleApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MatchMore.config(this, SdkConfigTest.API_KEY, true)
    }
}