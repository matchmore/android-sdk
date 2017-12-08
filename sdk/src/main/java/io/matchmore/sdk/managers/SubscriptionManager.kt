package io.matchmore.sdk.managers

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Subscription

class SubscriptionManager(private val manager: AlpsManager) {

    fun createSubscription(subscription: Subscription, deviceWithId: String? = null, success: SuccessCallback<Subscription>?, error: ErrorCallback?) {
        subscription.deviceId = deviceWithId ?: manager.deviceStore.main?.id
        manager.apiClient.subscriptionApi.createSubscription(subscription.deviceId!!, subscription)
                .async({
                    manager.subscriptionStore.create(it)
                    success?.invoke(it)
                }, error)
    }
}