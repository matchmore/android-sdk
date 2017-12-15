package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import net.jodah.concurrentunit.Waiter
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
abstract class BaseTest {

    // unfortunately we can't move that metho to @BeforeClass because robolectric RuntimeEnvironment.application is still null there
    fun initAndStartUsingMainDevice() {
        if (!MatchMore.isConfigured()) {
            MatchMore.config(MatchMoreConfig(
                    RuntimeEnvironment.application,
                    SdkConfigTest.API_KEY, SdkConfigTest.WORLD_ID,
                    false,
                    true))
        }
        MatchMore.instance.startUsingMainDevice({ _ ->
            waiter.assertEquals(1, MatchMore.instance.devices.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }

    companion object {
        val waiter = Waiter()

        @BeforeClass
        @JvmStatic
        fun setUp() {
            ShadowLog.stream = System.out
        }

        @AfterClass
        @JvmStatic
        fun removeDevices() {
            MatchMore.instance.devices.deleteAll({
                waiter.assertEquals(0, MatchMore.instance.devices.findAll().size)
                waiter.resume()
            }, waiter::fail)
            waiter.await(SdkConfigTest.TIMEOUT)
        }
    }
}