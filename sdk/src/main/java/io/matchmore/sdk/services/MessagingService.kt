package io.matchmore.sdk.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.Matchmore

class MessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        Log.i("fcm", "msg")
        if (Matchmore.isConfigured()) {
            val alps = Matchmore.instance as AlpsManager
            message.data?.let { alps.processPushNotification(it) }
        }
    }
}