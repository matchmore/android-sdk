package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.*
import org.junit.Test
import org.robolectric.annotation.Config

@Config(constants = BuildConfig::class, sdk = [26])
class TtlTest : BaseTest() {

    @Test
    fun getMatches() {
        initAndStartUsingMainDevice()
        val location = MatchmoreLocation(10.0, 10.0, 10.0)
        val matchMoreSdk = Matchmore.instance

        // create publication
        val publication = Publication("Test TTL", 2000.0, 20.0)
        publication.properties = hashMapOf("testMatchTTL" to true)
        matchMoreSdk.createPublicationForMainDevice(publication, { _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // create subscription
        val subscription = Subscription("Test TTL", 2000.0, 20.0)
        subscription.selector = "testMatchTTL = true"
        subscription.matchTTL = 2.0
        matchMoreSdk.createSubscriptionForMainDevice(subscription, { _ ->
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)


        // get a match
        var counter = 0
        val listener = { matches: Set<Match>, _: Device ->
            counter += matches.size
            matchMoreSdk.locationManager.sendLocation(location)
            if (counter == 2) waiter.resume()
        }
        matchMoreSdk.matchMonitor.addOnMatchListener(listener)
        matchMoreSdk.matchMonitor.startPollingMatches()
        matchMoreSdk.locationManager.sendLocation(location)

        waiter.await(SdkConfigTest.TIMEOUT)
        matchMoreSdk.matchMonitor.removeOnMatchListener(listener)
        matchMoreSdk.matchMonitor.stopPollingMatches()
        Thread.sleep(SdkConfigTest.TIMEOUT) //wait to be sure that last get matches call is done
    }
}