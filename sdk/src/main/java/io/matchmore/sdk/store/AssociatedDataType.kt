package io.matchmore.sdk.store

import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback

interface CRUD<T> : AsyncCreateable<T>, AsyncReadable<T>, AsyncUpdateable<T>, AsyncDeleteable<T>, AsyncClearable<T>
interface CRD<T> : AsyncCreateable<T>, AsyncReadable<T>, AsyncDeleteable<T>, AsyncClearable<T>

interface AsyncCreateable<T> {
    fun create(item: T)
}

interface AsyncReadable<T> {
    fun find(byId: String): T?
    fun findAll(success: SuccessCallback<List<T>>, error: ErrorCallback?)
}

interface AsyncUpdateable<T> {
    fun update(item: T, success: SuccessCallback<List<T>>?, error: ErrorCallback?)
}

interface AsyncDeleteable<T> {
    fun delete(item: T, error: ErrorCallback?)
}

interface AsyncClearable<T> {
    fun deleteAll(error: ErrorCallback?)
}
