package io.matchmore.sdk.rx

import io.matchmore.sdk.MatchMoreSdk
import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.*
import io.matchmore.sdk.monitoring.MatchMonitor
import io.matchmore.sdk.store.AsyncClearable
import io.matchmore.sdk.store.AsyncCreateable
import io.matchmore.sdk.store.AsyncDeleteable
import io.matchmore.sdk.store.AsyncUpdateable
import io.reactivex.*

private fun completableEmitter(emitter: CompletableEmitter): () -> Unit = { if (!emitter.isDisposed) emitter.onComplete() }

private fun <T> observableEmitter(emitter: ObservableEmitter<T>): (T) -> Unit = { item -> if (!emitter.isDisposed) emitter.onNext(item) }

private fun <T> successEmitter(emitter: SingleEmitter<T>): (T) -> Unit = { item -> if (!emitter.isDisposed) emitter.onSuccess(item) }

private fun errorEmitter(emitter: ObservableEmitter<*>): (Throwable) -> Unit = { throwable -> if (!emitter.isDisposed) emitter.onError(throwable) }

private fun errorEmitter(emitter: SingleEmitter<*>): (Throwable) -> Unit = { throwable -> if (!emitter.isDisposed) emitter.onError(throwable) }

private fun errorEmitter(emitter: CompletableEmitter): (Throwable) -> Unit = { throwable -> if (!emitter.isDisposed) emitter.onError(throwable) }

private fun <T> observableRx(function: ((SuccessCallback<T>, ErrorCallback) -> Unit)): Observable<T> = Observable.create<T>({ emitter ->
    function(observableEmitter(emitter), errorEmitter(emitter))
})

private fun <T> singleRx(function: ((SuccessCallback<T>, ErrorCallback) -> Unit)): Single<T> =
        Single.create<T>({ emitter ->
            function(successEmitter(emitter), errorEmitter(emitter))
        })

private fun completableRx(function: ((CompleteCallback, ErrorCallback) -> Unit)): Completable =
        Completable.create({ emitter ->
            function(completableEmitter(emitter), errorEmitter(emitter))
        })

fun MatchMoreSdk.rxStartUsingMainDevice(): Single<MobileDevice> = singleRx(this::startUsingMainDevice)

fun MatchMoreSdk.rxCreatePublication(publication: Publication, deviceWithId: String? = null)
        : Single<Publication> = singleRx { success, error -> createPublication(publication, deviceWithId, success, error) }

fun MatchMoreSdk.rxCreateSubscription(subscription: Subscription, deviceWithId: String? = null)
        : Single<Subscription> = singleRx { success, error -> createSubscription(subscription, deviceWithId, success, error) }

fun MatchMoreSdk.rxCreatePinDevice(pinDevice: PinDevice): Single<PinDevice> =
        singleRx { success, error -> createPinDevice(pinDevice, success, error) }

fun <T> AsyncCreateable<T>.rxCreate(item: T): Single<T> =
        singleRx { success, error -> create(item, success, error) }

fun <T> AsyncUpdateable<T>.rxUpdate(item: T): Single<T> =
        singleRx { success, error -> update(item, success, error) }

fun <T> AsyncDeleteable<T>.rxDelete(item: T): Completable =
        completableRx { complete, error -> delete(item, complete, error) }

fun AsyncClearable.rxDeleteAll(): Completable = completableRx { complete, error -> deleteAll(complete, error) }

private var matchObservable: Observable<Pair<Set<Match>, Device>>? = null

fun MatchMonitor.rxMatch(): Observable<Pair<Set<Match>, Device>> {
    if (matchObservable == null) {
        matchObservable = Observable.create<Pair<Set<Match>, Device>> { emitter ->
            val listener = { matches: Set<Match>, device: Device -> emitter.onNext(Pair(matches, device)) }
            addOnMatchListener(listener)
            emitter.setCancellable { removeOnMatchListener(listener) }
        }
    }
    return matchObservable!!
}