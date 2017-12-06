package io.matchmore.sdk

import io.matchmore.sdk.api.ApiClient
import net.jodah.concurrentunit.Waiter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class DevicesTest {

    private val waiter = Waiter()

    @Before
    fun setUp() {
        ApiClient.DEBUG = true
        ShadowLog.stream = System.out
        if (!MatchMore.isConfigured()) MatchMore.config(SdkConfigTest.API_KEY, SdkConfigTest.WORLD_ID)
    }

    @Test
    fun createMainDevice() {
        MatchMore.instance.startUsingMainDevice(success = { device -> waiter.resume() }, error = { waiter.fail(it) })
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}