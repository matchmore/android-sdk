package io.matchmore.sdk.rx

import io.matchmore.sdk.Matchmore
import io.matchmore.sdk.api.models.Subscription
import junit.framework.Assert.assertEquals
import org.junit.Test

class RxSubscriptionsTest : RxBaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = Matchmore.instance
        val subscription = Subscription("Test Topic", 20.0, 100000.0)
        matchMoreSdk.rxCreateSubscription(subscription).testAndWait()
        assertEquals(1, matchMoreSdk.subscriptions.findAll().size)

        matchMoreSdk.rxCreateSubscription(subscription).testAndWait()
        assertEquals(2, matchMoreSdk.subscriptions.findAll().size)

        matchMoreSdk.subscriptions.rxDelete(matchMoreSdk.subscriptions.findAll()[0]).testAndWait()
        assertEquals(1, matchMoreSdk.subscriptions.findAll().size)

        matchMoreSdk.subscriptions.rxDeleteAll().testAndWait()
        assertEquals(0, matchMoreSdk.subscriptions.findAll().size)
    }
}