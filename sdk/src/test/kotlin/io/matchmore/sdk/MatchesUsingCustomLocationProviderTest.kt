package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.*
import io.matchmore.sdk.managers.LocationSender
import io.matchmore.sdk.managers.MatchmoreLocationProvider
import org.junit.Test
import org.robolectric.annotation.Config

@Config(constants = BuildConfig::class)
class MatchesUsingCustomLocationProviderTest : BaseTest() {

    @Test
    fun getMatches() {
        initAndStartUsingMainDevice()
        val matchMoreSdk = Matchmore.instance

        // create publication
        val publication = Publication("Test Topic", 2000.0, 100000.0)
        publication.properties = hashMapOf("test" to true)
        matchMoreSdk.createPublicationForMainDevice(publication, { _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // create subscription
        val subscription = Subscription("Test Topic", 2000.0, 100000.0)
        subscription.selector = "test = true"
        matchMoreSdk.createSubscriptionForMainDevice(subscription, { _ ->
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val locationProvider = object : MatchmoreLocationProvider {
            override fun startUpdatingLocation(sender: LocationSender) {
                sender.sendLocation(MatchmoreLocation(createdAt = System.currentTimeMillis(), latitude = 80.0, longitude = 80.0))
            }

            override fun stopUpdatingLocation() {}
        }
        // update location
        matchMoreSdk.startUpdatingLocation(locationProvider)

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