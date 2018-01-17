package io.matchmore.sdk.rx

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.api.models.MobileDevice
import org.junit.Test

class RxDevicesTest : RxBaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance
        val device = MobileDevice("Test Device", platform = "Android")

        matchMoreSdk.devices.rxCreate(device).subscribe({ _ ->
            waiter.assertEquals(2, matchMoreSdk.devices.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.devices.rxDelete(matchMoreSdk.devices.findAll()[0]).subscribe({
            waiter.assertEquals(1, matchMoreSdk.devices.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.devices.rxDeleteAll().subscribe({
            waiter.assertEquals(0, matchMoreSdk.devices.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}