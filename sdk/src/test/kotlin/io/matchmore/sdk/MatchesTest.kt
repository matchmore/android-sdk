package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.Location
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test

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
        val location = Location(latitude = 54.414662, longitude = 18.625498)
        matchMoreSdk.locationManager.sendLocation(location) {
            waiter.assertEquals(location, matchMoreSdk.locationManager.lastLocation)
            waiter.resume()
        }
        waiter.await(SdkConfigTest.TIMEOUT)

        // get a match
        matchMoreSdk.matchMonitor.addOnMatchListener { matches, _ ->
            waiter.assertTrue(matches.size >= 0)
            waiter.resume()
        }
        matchMoreSdk.matchMonitor.startPollingMatches()
        waiter.await(SdkConfigTest.TIMEOUT)

        // delete sub
        matchMoreSdk.subscriptions.deleteAll({
            waiter.assertEquals(0, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // delete pub
        matchMoreSdk.publications.deleteAll({
            waiter.assertEquals(0, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}