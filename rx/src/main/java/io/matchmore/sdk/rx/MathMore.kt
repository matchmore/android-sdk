package io.matchmore.sdk.rx

import io.matchmore.sdk.MatchMoreSdk
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import io.reactivex.Single
import io.reactivex.SingleEmitter

fun <T> successEmitter(emitter: SingleEmitter<T>): (T) -> Unit = { item -> if (!emitter.isDisposed) emitter.onSuccess(item) }

fun errorEmitter(emitter: SingleEmitter<*>): (Throwable) -> Unit = { throwable -> if (!emitter.isDisposed) emitter.onError(throwable) }

fun <T> rx(function: ((SuccessCallback<T>, ErrorCallback) -> Unit)): Single<T> = Single.create<T>({ emitter ->
    function(successEmitter(emitter), errorEmitter(emitter))
})

fun MatchMoreSdk.rxStartUsingMainDevice(): Single<MobileDevice> = rx(this::startUsingMainDevice)

fun MatchMoreSdk.rxCreatePublication(publication: Publication): Single<Publication> = Single.create<Publication>({ emitter ->
    createPublication(publication, successEmitter(emitter), errorEmitter(emitter))
})

fun MatchMoreSdk.rxCreateSubscription(subscription: Subscription): Single<Subscription> = Single.create<Subscription>({ emitter ->
    createSubscription(subscription, successEmitter(emitter), errorEmitter(emitter))
})
