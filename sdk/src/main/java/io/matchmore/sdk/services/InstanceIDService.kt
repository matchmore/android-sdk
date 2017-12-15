package io.matchmore.sdk.services

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.MatchMore


class InstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        if (MatchMore.isConfigured()) {
            val alps = MatchMore.instance as AlpsManager
            val token = FirebaseInstanceId.getInstance().token
            token?.let { alps.registerDeviceToken(token) }
        }
    }
}