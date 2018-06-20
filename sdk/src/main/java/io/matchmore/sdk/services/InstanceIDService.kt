package io.matchmore.sdk.services

import com.google.firebase.iid.FirebaseInstanceIdService
import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.Matchmore


class InstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        if (Matchmore.isConfigured()) {
            val alps = Matchmore.instance as AlpsManager
            alps.registerDeviceToken(alps.getDeviceToken())
        }
    }
}