package io.matchmore.sdk.rx

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.MatchMore
import org.junit.Assert.assertEquals
import org.junit.Test

class RxPushTest : RxBaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()
        val matchMoreSdk = MatchMore.instance as AlpsManager
        val token = "testToken"
        matchMoreSdk.registerDeviceToken(token)
        assertEquals("fcm://$token", matchMoreSdk.main!!.deviceToken)
    }
}