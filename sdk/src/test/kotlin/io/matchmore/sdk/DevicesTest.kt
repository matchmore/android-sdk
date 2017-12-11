package io.matchmore.sdk

import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import net.jodah.concurrentunit.Waiter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class DevicesTest {

    private val waiter = Waiter()

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
        if (!MatchMore.isConfigured())
            MatchMore.config(MatchMoreConfig(
                    RuntimeEnvironment.application,
                    SdkConfigTest.API_KEY, SdkConfigTest.WORLD_ID,
                    false,
                    true))
    }

    @Test
    fun creations() {
        MatchMore.instance.startUsingMainDevice({ _ -> waiter.resume() }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val publication = Publication("Test Topic", 20.0, 100000.0)
        MatchMore.instance.createPublication(publication, { _ -> waiter.resume() }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val subscription = Subscription("Test Topic", 20.0, 100000.0, "")
        MatchMore.instance.createSubscription(subscription, { _ -> waiter.resume() }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}