package io.matchmore.sdk.rx

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.BuildConfig
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test
import org.robolectric.annotation.Config

@Config(constants = BuildConfig::class, sdk = [24])
class RxMatchesTest : RxBaseTest() {

    @Test
    fun getMatches() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance

        // create publication
        val publication = Publication("Test Topic", 2000.0, 100000.0)
        publication.properties = hashMapOf("test" to "true")
        matchMoreSdk.rxCreatePublication(publication).subscribe({ _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // create subscription
        val subscription = Subscription("Test Topic", 2000.0, 100000.0)
        subscription.selector = "test = 'true'"
        matchMoreSdk.rxCreateSubscription(subscription).subscribe({ _ ->
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // get a match
        val disposable = matchMoreSdk.matchMonitor.rxMatch().subscribe { (matches, _) ->
            waiter.assertTrue(matches.size >= 0)
            waiter.resume()
        }

        // update location
        mockLocation()
        matchMoreSdk.startUpdatingLocation()
        // start polling matches
        matchMoreSdk.matchMonitor.startPollingMatches()
        waiter.await(SdkConfigTest.TIMEOUT)

        // clear resources
        disposable.dispose()
        matchMoreSdk.matchMonitor.stopPollingMatches()
        matchMoreSdk.stopUpdatingLocation()
        // wait to be sure that last get matches call is done
        Thread.sleep(SdkConfigTest.TIMEOUT)
    }
}