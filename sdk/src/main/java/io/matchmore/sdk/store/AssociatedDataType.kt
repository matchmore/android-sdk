package io.matchmore.sdk.store

import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.utils.CallbacksGroup

interface CRUD<T> : AsyncCreateable<T>, AsyncReadable<T>, AsyncUpdateable<T>, AsyncDeleteable<T>, AsyncClearable

interface CRD<T> : AsyncCreateable<T>, AsyncReadable<T>, AsyncDeleteable<T>, AsyncClearable {
    override fun deleteAll(complete: CompleteCallback?, error: ErrorCallback?) {
        val group = CallbacksGroup(complete, error)
        findAll().forEach {
            group.enter()
            delete(it, group::complete, group::error)
        }
        group.end()
    }
}

interface AsyncCreateable<T> {
    fun create(item: T, success: SuccessCallback<T>?) = create(item, success, null)
    fun create(item: T) = create(item, null, null)
    fun create(item: T, success: SuccessCallback<T>? = null, error: ErrorCallback? = null)
}

interface AsyncReadable<T> {
    fun find(byId: String): T?
    fun findAll(): List<T>
}

interface AsyncUpdateable<T> {
    fun update(item: T, success: SuccessCallback<T>?) = update(item, success, null)
    fun update(item: T) = update(item, null, null)
    fun update(item: T, success: SuccessCallback<T>? = null, error: ErrorCallback? = null)
}

interface AsyncDeleteable<T> {
    fun delete(item: T, complete: CompleteCallback?) = delete(item, complete, null)
    fun delete(item: T, error: ErrorCallback?) = delete(item, null, error)
    fun delete(item: T) = delete(item, null, null)
    fun delete(item: T, complete: CompleteCallback? = null, error: ErrorCallback? = null)
}

interface AsyncClearable {
    fun deleteAll(complete: CompleteCallback?) = deleteAll(complete, null)
    fun deleteAll(error: ErrorCallback?) = deleteAll(null, error)
    fun deleteAll() = deleteAll(null, null)
    fun deleteAll(complete: CompleteCallback? = null, error: ErrorCallback? = null)
}
