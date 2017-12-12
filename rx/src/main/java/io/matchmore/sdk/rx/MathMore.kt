package io.matchmore.sdk.rx

import io.matchmore.sdk.MatchMoreSdk
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import io.reactivex.Single
import io.reactivex.SingleEmitter

private fun <T> successEmitter(emitter: SingleEmitter<T>): (T) -> Unit = { item -> if (!emitter.isDisposed) emitter.onSuccess(item) }

private fun errorEmitter(emitter: SingleEmitter<*>): (Throwable) -> Unit = { throwable -> if (!emitter.isDisposed) emitter.onError(throwable) }

private fun <T> rx(function: ((SuccessCallback<T>, ErrorCallback) -> Unit)): Single<T> = Single.create<T>({ emitter ->
    function(successEmitter(emitter), errorEmitter(emitter))
})

fun MatchMoreSdk.rxStartUsingMainDevice(): Single<MobileDevice> = rx(this::startUsingMainDevice)

fun MatchMoreSdk.rxCreatePublication(publication: Publication): Single<Publication>
        = rx { success, error -> createPublication(publication, success, error) }

fun MatchMoreSdk.rxCreateSubscription(subscription: Subscription): Single<Subscription>
        = rx { success, error -> createSubscription(subscription, success, error) }

