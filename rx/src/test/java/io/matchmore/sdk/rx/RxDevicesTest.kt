package io.matchmore.sdk.rx

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.Matchmore
import io.matchmore.sdk.api.models.MobileDevice
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

class RxDevicesTest : RxBaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = Matchmore.instance
        val device = MobileDevice("Test Device", platform = "Android")

        matchMoreSdk.devices.rxCreate(device).test().awaitDone(SdkConfigTest.TIMEOUT,TimeUnit.MILLISECONDS)
        assertEquals(2, matchMoreSdk.devices.findAll().size)

        matchMoreSdk.devices.rxDelete(matchMoreSdk.devices.findAll()[0]).test().awaitDone(SdkConfigTest.TIMEOUT,TimeUnit.MILLISECONDS)
        assertEquals(1, matchMoreSdk.devices.findAll().size)

        matchMoreSdk.devices.rxDeleteAll().test().awaitDone(SdkConfigTest.TIMEOUT,TimeUnit.MILLISECONDS)
        assertEquals(0, matchMoreSdk.devices.findAll().size)
    }
}