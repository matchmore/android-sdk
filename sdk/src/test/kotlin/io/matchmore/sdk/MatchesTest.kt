package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.Match
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test
import org.robolectric.annotation.Config

@Config(constants = BuildConfig::class, sdk = [24])
class MatchesTest : BaseTest() {

    @Test
    fun getMatches() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance

        // create publication
        val publication = Publication("Test Topic", 2000.0, 100000.0)
        publication.properties = hashMapOf("test" to "true")
        matchMoreSdk.createPublication(publication, { _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // create subscription
        val subscription = Subscription("Test Topic", 2000.0, 100000.0)
        subscription.selector = "test = 'true'"
        matchMoreSdk.createSubscription(subscription, { _ ->
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // update location
        mockLocation()
        matchMoreSdk.startUpdatingLocation()

        // get a match
        val listener = { matches: Set<Match>, _: Device ->
            waiter.assertTrue(matches.size >= 0)
            waiter.resume()
        }
        matchMoreSdk.matchMonitor.addOnMatchListener(listener)
        matchMoreSdk.matchMonitor.startPollingMatches()
        waiter.await(SdkConfigTest.TIMEOUT)
        matchMoreSdk.matchMonitor.removeOnMatchListener(listener)
        matchMoreSdk.matchMonitor.stopPollingMatches()
        matchMoreSdk.stopUpdatingLocation()
        Thread.sleep(SdkConfigTest.TIMEOUT) //wait to be sure that last get matches call is done
    }
}