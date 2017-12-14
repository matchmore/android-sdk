package io.matchmore.sdk.store

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Subscription

class SubscriptionStore(private val manager: AlpsManager) : CRD<Subscription>,
        Store<Subscription>(manager.persistenceManager, SUBSCRIPTIONS_FILE) {

    init {
        this.items = manager.persistenceManager.readData<List<Subscription>>(SUBSCRIPTIONS_FILE) ?: arrayListOf()
    }

    fun createSubscription(subscription: Subscription, deviceWithId: String? = null, success: SuccessCallback<Subscription>?, error: ErrorCallback?) {
        subscription.deviceId = deviceWithId ?: manager.main?.id
        create(subscription, success, error)
    }

    override fun create(item: Subscription, success: SuccessCallback<Subscription>?, error: ErrorCallback?) {
        manager.apiClient.subscriptionApi.createSubscription(item.deviceId!!, item)
                .async({
                    createData(it)
                    success?.invoke(it)
                }, error)
    }

    override fun delete(item: Subscription, complete: CompleteCallback?, error: ErrorCallback?) {
        manager.apiClient.subscriptionApi.deleteSubscription(item.deviceId!!, item.id!!)
                .async({
                    deleteData(item)
                    complete?.invoke()
                }, error)
    }

    companion object {
        private const val SUBSCRIPTIONS_FILE = "kSubscriptionsFile.Alps"
    }
}