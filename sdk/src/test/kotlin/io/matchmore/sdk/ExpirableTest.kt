package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test

class ExpirableTest : BaseTest() {
    @Test
    fun testExpirablePubSub() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance

        // create publications
        val publication = Publication("Test Topic", 2000.0, 100.0)
        matchMoreSdk.createPublicationForMainDevice(publication, { _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val expiringPublication = Publication("Expiring Topic", 2000.0, 1.0)
        matchMoreSdk.createPublicationForMainDevice(expiringPublication, { _ ->
            waiter.assertEquals(2, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // create subscriptions
        val subscription = Subscription("Test Topic", 2000.0, 100.0)
        matchMoreSdk.createSubscriptionForMainDevice(subscription, { _ ->
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val expiringSubscription = Subscription("Expiring Topic", 2000.0, 1.0)
        matchMoreSdk.createSubscriptionForMainDevice(expiringSubscription, { _ ->
            waiter.assertEquals(2, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // wait 2 second
        Thread.sleep(2_000)

        // get publications
        waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)

        // get subscriptions
        waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
    }
}