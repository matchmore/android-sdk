package io.matchmore.sdk.rx

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
                    RuntimeEnvironment.application,
                    SdkConfigTest.API_KEY, SdkConfigTest.WORLD_ID,
                    false,
                    true))
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