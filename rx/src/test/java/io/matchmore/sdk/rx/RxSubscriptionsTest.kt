package io.matchmore.sdk.rx

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test

class RxSubscriptionsTest : RxBaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance
        val subscription = Subscription("Test Topic", 20.0, 100000.0)
        matchMoreSdk.rxCreateSubscription(subscription).subscribe({ _ ->
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.rxCreateSubscription(subscription).subscribe({ _ ->
            waiter.assertEquals(2, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.subscriptions.rxDelete(matchMoreSdk.subscriptions.findAll()[0]).subscribe({
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.subscriptions.rxDeleteAll().subscribe({
            waiter.assertEquals(0, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}