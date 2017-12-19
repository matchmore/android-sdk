package io.matchmore.sdk

import io.matchmore.sdk.api.models.Location
import io.matchmore.sdk.api.models.PinDevice
import io.matchmore.config.SdkConfigTest
import org.junit.Test

class PinDevicesTest : BaseTest() {

    @Test
    fun testPinDeviceCreation() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance
        var pinDevice = PinDevice("Test Pin", location = Location(latitude = 2.0, longitude = 2.0))

        matchMoreSdk.devices.create(pinDevice, { _ ->
            pinDevice = matchMoreSdk.devices.findAll().filterIsInstance<PinDevice>().first()
            waiter.assertEquals(1, matchMoreSdk.devices.findAll().filterIsInstance<PinDevice>().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.devices.delete(pinDevice, {
            waiter.assertEquals(0, matchMoreSdk.devices.findAll().filterIsInstance<PinDevice>().size)
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