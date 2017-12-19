package io.matchmore.sdk

import io.matchmore.sdk.api.models.Location
import io.matchmore.sdk.api.models.PinDevice
import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test

class PinDevicesTest : BaseTest() {

    @Test
    fun testPinDeviceCreation() {
        init()

        val matchMoreSdk = MatchMore.instance
        var pinDevice = PinDevice("Test Pin", location = Location(latitude = 2.0, longitude = 2.0))

        matchMoreSdk.createPinDevice(pinDevice, {
            pinDevice = it
            waiter.assertEquals(1, matchMoreSdk.devices.findAll().filterIsInstance<PinDevice>().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val subscription = Subscription("Test Topic", 20.0, 100000.0)
        matchMoreSdk.createSubscription(subscription, pinDevice.id, {
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val publication = Publication("Test Topic", 20.0, 100000.0)
        matchMoreSdk.createPublication(publication, pinDevice.id, { _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.devices.delete(pinDevice, {
            waiter.assertEquals(0, matchMoreSdk.subscriptions.findAll().size)
            waiter.assertEquals(0, matchMoreSdk.publications.findAll().size)
            waiter.assertEquals(0, matchMoreSdk.devices.findAll().filterIsInstance<PinDevice>().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}