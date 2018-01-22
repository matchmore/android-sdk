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

@Config(constants = BuildConfig::class, sdk = [24])
class RxMatchesTest : RxBaseTest() {

    @Test
    fun getMatches() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance

        // create publication
        val publication = Publication("Test Topic", 2000.0, 100000.0)
        publication.properties = hashMapOf("test" to "true")
        matchMoreSdk.rxCreatePublication(publication).testAndWait()
        assertEquals(1, matchMoreSdk.publications.findAll().size)

        // create subscription
        val subscription = Subscription("Test Topic", 2000.0, 100000.0)
        subscription.selector = "test = 'true'"
        matchMoreSdk.rxCreateSubscription(subscription).testAndWait()
        assertEquals(1, matchMoreSdk.subscriptions.findAll().size)

        val matchTest = matchMoreSdk.matchMonitor.rxMatch().test()

        // update location
        mockLocation()
        matchMoreSdk.startUpdatingLocation()
        // start polling matches
        matchMoreSdk.matchMonitor.startPollingMatches()
        matchTest.awaitDone(SdkConfigTest.TIMEOUT, TimeUnit.MILLISECONDS)
                .assertValueAt(0) { (matches, _) -> matches.isNotEmpty() }

        // clear resources
        matchMoreSdk.matchMonitor.stopPollingMatches()
        matchMoreSdk.stopUpdatingLocation()
    }
}