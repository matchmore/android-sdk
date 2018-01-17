package io.matchmore.sdk.rx

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test

class RxExpirableTest : RxBaseTest() {
    @Test
    fun testExpirablePubSub() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance

        // create publications
        val publication = Publication("Test Topic", 2000.0, 100.0)
        matchMoreSdk.rxCreatePublication(publication).subscribe({ _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val expiringPublication = Publication("Expiring Topic", 2000.0, 1.0)
        matchMoreSdk.rxCreatePublication(expiringPublication).subscribe({ _ ->
            waiter.assertEquals(2, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // create subscriptions
        val subscription = Subscription("Test Topic", 2000.0, 100.0)
        matchMoreSdk.rxCreateSubscription(subscription).subscribe({ _ ->
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val expiringSubscription = Subscription("Expiring Topic", 2000.0, 1.0)
        matchMoreSdk.rxCreateSubscription(expiringSubscription).subscribe({ _ ->
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