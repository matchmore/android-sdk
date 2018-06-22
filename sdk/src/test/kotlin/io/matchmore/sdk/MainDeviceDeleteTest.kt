package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.Device
import io.matchmore.sdk.api.models.Match
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test
import org.robolectric.annotation.Config

@Config(constants = BuildConfig::class, sdk = [24])
class MainDeviceDeleteTest : BaseTest() {

    @Test
    fun mainDeviceReinit() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = Matchmore.instance

        // create publication
        val publication = Publication("Test Topic X", 2000.0, 100000.0)
        publication.properties = hashMapOf("test" to true)
        matchMoreSdk.createPublicationForMainDevice(publication, { _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // create subscription
        val subscription = Subscription("Test Topic X", 2000.0, 100000.0)
        subscription.selector = "test = true"
        matchMoreSdk.createSubscriptionForMainDevice(subscription, { _ ->
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.publications.deleteAll({ ->
            waiter.assertEquals(0, Matchmore.instance.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.subscriptions.deleteAll({ ->
            waiter.assertEquals(0, Matchmore.instance.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.devices.deleteAll({ ->
            waiter.assertEquals(0, Matchmore.instance.devices.findAll().size)
            waiter.assertEquals(null, Matchmore.instance.main)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        Matchmore.instance.startUsingMainDevice({ _ ->
            waiter.assertEquals(1, Matchmore.instance.devices.findAll().size)
            waiter.assertNotNull(Matchmore.instance.main)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // create publication
        val publication2 = Publication("Test Topic X", 2000.0, 100000.0)
        publication2.properties = hashMapOf("test" to true)
        matchMoreSdk.createPublicationForMainDevice(publication2, { _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // create subscription
        val subscription2 = Subscription("Test Topic X", 2000.0, 100000.0)
        subscription2.selector = "test = true"
        matchMoreSdk.createSubscriptionForMainDevice(subscription2, { _ ->
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        // update location
        BaseTest.mockLocation()
        matchMoreSdk.startUpdatingLocation()

        // get a match
        val listener = { matches: Set<Match>, _: Device ->
            println("SIZE: ${matches.size}")
            waiter.assertTrue(matches.size > 0)
            waiter.resume()
        }
        matchMoreSdk.matchMonitor.addOnMatchListener(listener)
        matchMoreSdk.matchMonitor.startPollingMatches()
        waiter.await(SdkConfigTest.TIMEOUT)
        matchMoreSdk.matchMonitor.removeOnMatchListener(listener)
        matchMoreSdk.matchMonitor.stopPollingMatches()
        matchMoreSdk.stopUpdatingLocation()
        Thread.sleep(SdkConfigTest.TIMEOUT) //wait to be sure that last get matches call is done
    }
}