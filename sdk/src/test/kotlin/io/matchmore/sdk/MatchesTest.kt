package io.matchmore.sdk

import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.Match
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import io.matchmore.sdk.monitoring.*
import org.junit.Test

class MatchesTest : BaseTest() {

    class TestMatchListener(private val finished: (numberOfMatches: Int)->Unit): MatchListener {
        override fun onReceiveMatches(matches: Set<Match>, forDevice: Device) {
            finished(matches.size)
        }
    }

    @Test
    fun test() {
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

        // get a match
        val testMatchListener = TestMatchListener { numberOfMatches ->
            waiter.assertEquals(1, numberOfMatches)
            waiter.resume()
        }
        matchMoreSdk.matchMonitor.monitoredDevices.add(matchMoreSdk.main!!)
        matchMoreSdk.matchMonitor.addOnMatchListener(testMatchListener)
        matchMoreSdk.matchMonitor.startPollingMatches()
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}