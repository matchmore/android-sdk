package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.Match
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test
import org.robolectric.annotation.Config

// Workaround for a bug that after openSocketForMatches all other tests throw SocketTimeoutException
// SDK 23 makes that test run in other environment
@Config(constants = BuildConfig::class, sdk = [23])
class SocketTest : BaseTest() {

    @Test
    fun getMatchesUsingSocket() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance

        // open socket
        matchMoreSdk.matchMonitor.openSocketForMatches()

        // start listening for matches
        val listener = { matches: Set<Match>, _: Device ->
            waiter.assertTrue(matches.size >= 0)
            matchMoreSdk.matchMonitor.closeSocketForMatches()
            waiter.resume()
        }
        matchMoreSdk.matchMonitor.addOnMatchListener(listener)

        // create publication
        val publication = Publication("Test Topic", 2000.0, 100000.0)
        publication.properties = hashMapOf("test" to "true")
        matchMoreSdk.createPublication(publication, { _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
        }, waiter::fail)

        // create subscription
        val subscription = Subscription("Test Topic", 2000.0, 100000.0)
        subscription.selector = "test = 'true'"
        subscription.pushers = mutableListOf("ws")
        matchMoreSdk.createSubscription(subscription, { _ ->
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
        }, waiter::fail)

        // update location
        BaseTest.mockLocation()
        matchMoreSdk.startUpdatingLocation()

        waiter.await(SdkConfigTest.TIMEOUT)
        matchMoreSdk.matchMonitor.removeOnMatchListener(listener)
        matchMoreSdk.stopUpdatingLocation()
    }
}