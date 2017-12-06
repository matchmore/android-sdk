package io.matchmore.sdk

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

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }


    @Test
    fun configCorrectly() {
        MatchMore.config(SdkConfigTest.apiKey, SdkConfigTest.worldId)
        val waiter = Waiter()

        MatchMore.instance.startUsingMainDevice(success = { device -> waiter.resume() }, error = { waiter.fail(it) })

        waiter.await(10000);
    }
}