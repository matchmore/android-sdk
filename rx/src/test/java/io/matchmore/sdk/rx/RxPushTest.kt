package io.matchmore.sdk.rx

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.Matchmore
import org.junit.Assert.assertEquals
import org.junit.Test

class RxPushTest : RxBaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()
        val matchMoreSdk = Matchmore.instance as AlpsManager
        val token = "testToken"
        matchMoreSdk.registerDeviceToken(token)
        assertEquals("fcm://$token", matchMoreSdk.main!!.deviceToken)
    }
}