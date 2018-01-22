package io.matchmore.sdk

import org.junit.Test
import kotlin.test.assertEquals

class PushTest : BaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()
        val matchMoreSdk = MatchMore.instance as AlpsManager
        val token = "testToken"
        matchMoreSdk.registerDeviceToken(token)
        assertEquals("fcm://$token", matchMoreSdk.main!!.deviceToken)
    }
}