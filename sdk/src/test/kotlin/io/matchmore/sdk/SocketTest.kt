package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.Location
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test

class SocketTest : BaseTest() {

    @Test
    fun getMatchesUsingSocket() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance

        // create publication
        val publication = Publication("Test Topic", 2000.0, 100000.0)
        publication.properties = mutableMapOf("test" to "true")
        matchMoreSdk.createPublication(publication, { _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // open socket
        matchMoreSdk.matchMonitor.openSocketForMatches()

        // start listening for matches
        matchMoreSdk.matchMonitor.addOnMatchListener { matches, _ ->
            waiter.assertTrue(matches.size >= 0)
            waiter.resume()
        }
        // create subscription
        val subscription = Subscription("Test Topic", 2000.0, 100000.0)
        subscription.selector = "test = 'true'"
        subscription.pushers = mutableListOf("ws")
        matchMoreSdk.createSubscription(subscription, null, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}