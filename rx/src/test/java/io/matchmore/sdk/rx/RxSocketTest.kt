package io.matchmore.sdk.rx

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.BuildConfig
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit

// Workaround for a bug that after openSocketForMatches all other tests throw SocketTimeoutException
// SDK 23 makes that test run in other environment
@Config(constants = BuildConfig::class, sdk = [23])
class RxSocketTest : RxBaseTest() {

    @Test
    fun getMatchesUsingSocket() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance

        // open socket
        matchMoreSdk.matchMonitor.openSocketForMatches()

        // start listening for matches
        val matchTest = matchMoreSdk.matchMonitor.rxMatch().test()

        // create publication
        val publication = Publication("Test Topic", 2000.0, 100000.0)
        publication.properties = hashMapOf("test" to "true")
        matchMoreSdk.rxCreatePublication(publication).testAndWait()
        assertEquals(1, matchMoreSdk.publications.findAll().size)

        // create subscription
        val subscription = Subscription("Test Topic", 2000.0, 100000.0)
        subscription.selector = "test = 'true'"
        subscription.pushers = mutableListOf("ws")
        matchMoreSdk.rxCreateSubscription(subscription).testAndWait()
        assertEquals(1, matchMoreSdk.subscriptions.findAll().size)

        // update location
        mockLocation()
        matchMoreSdk.startUpdatingLocation()

        matchTest.awaitDone(SdkConfigTest.TIMEOUT, TimeUnit.MILLISECONDS)
                .assertValueAt(0)  { (matches, _) -> matches.isNotEmpty() }
        matchMoreSdk.stopUpdatingLocation()
    }
}