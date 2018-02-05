package io.matchmore.sdk.rx

import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.api.models.MatchMoreLocation
import io.matchmore.sdk.api.models.PinDevice
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import junit.framework.Assert.assertEquals
import org.junit.Test

class RxPinDevicesTest : RxBaseTest() {

    @Test
    fun testPinDeviceCreation() {
        init()

        val matchMoreSdk = MatchMore.instance
        var pinDevice = PinDevice("Test Pin", location = MatchMoreLocation(latitude = 2.0, longitude = 2.0))

        matchMoreSdk.rxCreatePinDevice(pinDevice).testAndWait().assertValue {
            pinDevice = it
            true
        }

        val subscription = Subscription("Test Topic", 20.0, 100000.0)
        matchMoreSdk.rxCreateSubscription(subscription, pinDevice.id).testAndWait()
        assertEquals(1, matchMoreSdk.subscriptions.findAll().size)

        val publication = Publication("Test Topic", 20.0, 100000.0)
        matchMoreSdk.rxCreatePublication(publication, pinDevice.id).testAndWait()
        assertEquals(1, matchMoreSdk.publications.findAll().size)

        matchMoreSdk.devices.rxDelete(pinDevice).testAndWait()
        assertEquals(0, matchMoreSdk.subscriptions.findAll().size)
        assertEquals(0, matchMoreSdk.publications.findAll().size)
        assertEquals(0, matchMoreSdk.devices.findAll().filterIsInstance<PinDevice>().size)
    }
}