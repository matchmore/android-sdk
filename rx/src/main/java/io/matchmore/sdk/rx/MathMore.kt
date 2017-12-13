package io.matchmore.sdk.rx

import io.matchmore.sdk.MatchMoreSdk
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.MobileDevice
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription
import io.matchmore.sdk.store.AsyncClearable
import io.matchmore.sdk.store.AsyncCreateable
import io.matchmore.sdk.store.AsyncDeleteable
import io.matchmore.sdk.store.AsyncUpdateable
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Single
import io.reactivex.SingleEmitter

private fun completableEmitter(emitter: CompletableEmitter): () -> Unit = { if (!emitter.isDisposed) emitter.onComplete() }

private fun <T> successEmitter(emitter: SingleEmitter<T>): (T) -> Unit = { item -> if (!emitter.isDisposed) emitter.onSuccess(item) }

private fun errorEmitter(emitter: SingleEmitter<*>): (Throwable) -> Unit = { throwable -> if (!emitter.isDisposed) emitter.onError(throwable) }

private fun errorEmitter(emitter: CompletableEmitter): (Throwable) -> Unit = { throwable -> if (!emitter.isDisposed) emitter.onError(throwable) }

private fun <T> singleRx(function: ((SuccessCallback<T>, ErrorCallback) -> Unit)): Single<T> = Single.create<T>({ emitter ->
    function(successEmitter(emitter), errorEmitter(emitter))
})

private fun completableRx(function: ((CompleteCallback, ErrorCallback) -> Unit)): Completable = Completable.create({ emitter ->
    function(completableEmitter(emitter), errorEmitter(emitter))
})

fun MatchMoreSdk.rxStartUsingMainDevice(): Single<MobileDevice> = singleRx(this::startUsingMainDevice)

fun MatchMoreSdk.rxCreatePublication(publication: Publication): Single<Publication>
        = singleRx { success, error -> createPublication(publication, success, error) }

fun MatchMoreSdk.rxCreateSubscription(subscription: Subscription): Single<Subscription>
        = singleRx { success, error -> createSubscription(subscription, success, error) }

fun <T> AsyncCreateable<T>.rxCreate(item: T): Single<T>
        = singleRx { success, error -> create(item, success, error) }

fun <T> AsyncUpdateable<T>.rxUpdate(item: T): Single<T>
        = singleRx { success, error -> update(item, success, error) }

fun <T> AsyncDeleteable<T>.rxDelete(item: T): Completable
        = completableRx { complete, error -> delete(item, complete, error) }

fun AsyncClearable.rxDeleteAll(): Completable
        = completableRx { complete, error -> deleteAll(complete, error) }