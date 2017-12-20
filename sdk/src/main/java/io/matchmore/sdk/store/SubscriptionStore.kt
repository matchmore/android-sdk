package io.matchmore.sdk.store

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Subscription
import io.matchmore.sdk.utils.withoutExpired

class SubscriptionStore(private val manager: AlpsManager) : CRD<Subscription>,
        Store<Subscription>(manager.persistenceManager, SUBSCRIPTIONS_FILE) {

    private var _items = listOf<Subscription>()
    override var items: List<Subscription>
        get() {
            return _items.withoutExpired()
        }
        set(value) {
            Thread({ persistenceManager.writeData(value, file) }).start()
            _items = value
        }
    init {
        items = manager.persistenceManager.readData<List<Subscription>>(SUBSCRIPTIONS_FILE)?.withoutExpired() ?: arrayListOf()
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

    var onDeviceDelete: DeviceDeleteListener = { id ->
        items = items.filter { it.deviceId != id }
    }

    companion object {
        private const val SUBSCRIPTIONS_FILE = "kSubscriptionsFile.Alps"
    }
}