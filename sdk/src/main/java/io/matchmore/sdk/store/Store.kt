package io.matchmore.sdk.store

import com.google.gson.reflect.TypeToken
import io.matchmore.sdk.api.models.HasId
import io.matchmore.sdk.utils.PersistenceManager

open class Store<T : HasId>(private val persistenceManager: PersistenceManager, private val file: String) {

    private val type = object : TypeToken<List<T>>() {}.type
    private var items = listOf<T>()
        set(value) {
            Thread({ persistenceManager.writeData(value, file) }).start()
            field = value
        }

    init {
        this.items = persistenceManager.readData(file, type) ?: listOf()
    }

    fun find(byId: String) = findAll().firstOrNull { it.id == byId }

    fun findAll() = items

    protected fun create(item: T) {
        items += item
    }

    protected fun delete(item: T) {
        items -= item
    }

    protected fun deleteAll() {
        items = listOf()
    }
}