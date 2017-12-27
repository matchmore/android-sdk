package io.matchmore.sdk.rx

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.MatchMoreConfig
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
class RxUnitTest {

    private val waiter = Waiter()

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
        if (!MatchMore.isConfigured())
            MatchMore.config(MatchMoreConfig(
                    context = RuntimeEnvironment.application,
                    apiKey = SdkConfigTest.API_KEY, worldId = SdkConfigTest.WORLD_ID,
                    callbackInUIThread = false,
                    debugLog = true))
    }

    @Test
    fun creations() {
        val matchMore = MatchMore.instance
        matchMore.rxStartUsingMainDevice().subscribe({ _ -> waiter.resume() }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val publication = Publication("Test Topic", 20.0, 100000.0)
        matchMore.rxCreatePublication(publication).subscribe({ _ -> waiter.resume() }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val subscription = Subscription("Test Topic", 20.0, 100000.0, "")
        matchMore.rxCreateSubscription(subscription).subscribe({ _ -> waiter.resume() }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMore.publications.rxDeleteAll().subscribe(waiter::resume, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMore.subscriptions.rxDeleteAll().subscribe(waiter::resume, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMore.devices.rxDeleteAll().subscribe(waiter::resume, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}