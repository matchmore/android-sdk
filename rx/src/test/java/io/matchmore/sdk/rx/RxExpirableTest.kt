package io.matchmore.sdk.rx

import io.matchmore.sdk.Matchmore
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import junit.framework.Assert.assertEquals
import org.junit.Test

class RxExpirableTest : RxBaseTest() {
    @Test
    fun testExpirablePubSub() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = Matchmore.instance

        // create publications
        val publication = Publication("Test Topic", 2000.0, 100.0)
        matchMoreSdk.rxCreatePublication(publication).testAndWait()
        assertEquals(1, matchMoreSdk.publications.findAll().size)

        val expiringPublication = Publication("Expiring Topic", 2000.0, 1.0)
        matchMoreSdk.rxCreatePublication(expiringPublication).testAndWait()
        assertEquals(2, matchMoreSdk.publications.findAll().size)

        // create subscriptions
        val subscription = Subscription("Test Topic", 2000.0, 100.0)
        matchMoreSdk.rxCreateSubscription(subscription).testAndWait()
        assertEquals(1, matchMoreSdk.subscriptions.findAll().size)

        val expiringSubscription = Subscription("Expiring Topic", 2000.0, 1.0)
        matchMoreSdk.rxCreateSubscription(expiringSubscription).testAndWait()
        assertEquals(2, matchMoreSdk.subscriptions.findAll().size)

        // wait 2 second
        Thread.sleep(2_000)

        // get publications
        assertEquals(1, matchMoreSdk.publications.findAll().size)

        // get subscriptions
        assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
    }
}