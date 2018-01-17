package io.matchmore.sdk.rx

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.api.models.MatchMoreLocation
import io.matchmore.sdk.api.models.PinDevice
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import org.junit.Test

class RxPinDevicesTest : RxBaseTest() {

    @Test
    fun testPinDeviceCreation() {
        init()

        val matchMoreSdk = MatchMore.instance
        var pinDevice = PinDevice("Test Pin", location = MatchMoreLocation(latitude = 2.0, longitude = 2.0))

        matchMoreSdk.rxCreatePinDevice(pinDevice).subscribe({
            pinDevice = it
            waiter.assertEquals(1, matchMoreSdk.devices.findAll().filterIsInstance<PinDevice>().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val subscription = Subscription("Test Topic", 20.0, 100000.0)
        matchMoreSdk.rxCreateSubscription(subscription, pinDevice.id).subscribe({
            waiter.assertEquals(1, matchMoreSdk.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        val publication = Publication("Test Topic", 20.0, 100000.0)
        matchMoreSdk.rxCreatePublication(publication, pinDevice.id).subscribe({ _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.devices.rxDelete(pinDevice).subscribe({
            waiter.assertEquals(0, matchMoreSdk.subscriptions.findAll().size)
            waiter.assertEquals(0, matchMoreSdk.publications.findAll().size)
            waiter.assertEquals(0, matchMoreSdk.devices.findAll().filterIsInstance<PinDevice>().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}