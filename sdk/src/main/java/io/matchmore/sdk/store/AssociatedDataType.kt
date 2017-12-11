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
    fun create(item: T, success: SuccessCallback<T>? = null, error: ErrorCallback? = null)
}

interface AsyncReadable<T> {
    fun find(byId: String): T?
    fun findAll(): List<T>
}

interface AsyncUpdateable<T> {
    fun update(item: T, success: SuccessCallback<T>? = null, error: ErrorCallback? = null)
}

interface AsyncDeleteable<T> {
    fun delete(item: T, complete: CompleteCallback? = null, error: ErrorCallback? = null)
}

interface AsyncClearable {
    fun deleteAll(complete: CompleteCallback? = null, error: ErrorCallback? = null)
}
