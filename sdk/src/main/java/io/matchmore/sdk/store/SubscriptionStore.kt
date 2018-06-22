package io.matchmore.sdk.store

import io.matchmore.sdk.AlpsManager
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.async
import io.matchmore.sdk.api.models.Subscription
import io.matchmore.sdk.utils.unwrap
import io.matchmore.sdk.utils.withoutExpired

class SubscriptionStore(private val manager: AlpsManager) : CRD<Subscription>,
        Store<Subscription>(manager.persistenceManager, SUBSCRIPTIONS_FILE) {

    override var items = listOf<Subscription>()
        get() = field.withoutExpired()
        set(value) {
            Thread({ manager.persistenceManager.writeData(value, SUBSCRIPTIONS_FILE) }).start()
            field = value
        }

    init {
        items = manager.persistenceManager.readData<List<Subscription>>(SUBSCRIPTIONS_FILE)?.withoutExpired() ?: listOf()
    }

    fun createSubscription(subscription: Subscription, deviceWithId: String? = null, success: SuccessCallback<Subscription>?, error: ErrorCallback?) {
        subscription.deviceId = deviceWithId ?: subscription.deviceId
        create(subscription, success, error)
    }

    override fun create(item: Subscription, success: SuccessCallback<Subscription>?, error: ErrorCallback?) {
        item.deviceId = item.deviceId ?: manager.devices.main?.id
        item?.deviceId?.let { deviceId ->
            manager.apiClient.subscriptionApi.createSubscription(deviceId, item)
                    .async({
                        createData(it)
                        success?.invoke(it)
                    }, error)
        } ?: run {
            error(Throwable("Subscription has to have device ID."))
        }
    }

    override fun delete(item: Subscription, complete: CompleteCallback?, error: ErrorCallback?) {
        unwrap(item?.deviceId, item?.id, { deviceId, id ->
            manager.apiClient.subscriptionApi.deleteSubscription(deviceId, id)
                    .async({
                        deleteData(item)
                        complete?.invoke()
                    }, error)
        }, {
            error(Throwable("Subscription has to have ID and device ID."))
        })
    }

    var onDeviceDelete: DeviceDeleteListener = { id ->
        items = items.filter { it.deviceId != id }
    }

    companion object {
        private const val SUBSCRIPTIONS_FILE = "kSubscriptionsFile.Alps"
    }
}