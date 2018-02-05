package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.MobileDevice
import org.junit.Test

class DevicesTest : BaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance
        val device = MobileDevice("Test Device", platform = "Android")

        matchMoreSdk.devices.create(device, { _ ->
            waiter.assertEquals(2, matchMoreSdk.devices.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.devices.delete(matchMoreSdk.devices.findAll()[0], {
            waiter.assertEquals(1, matchMoreSdk.devices.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.devices.deleteAll({
            waiter.assertEquals(0, matchMoreSdk.devices.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}