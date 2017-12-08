package io.matchmore.sdk.store

import com.google.gson.reflect.TypeToken
import io.matchmore.sdk.api.models.HasId

open class Store<T: HasId>(private val persistenceManager: PersistenceManager, private val file: String) : CRD<T> {

    private val type = object : TypeToken<List<T>>() {}.type
    private var items = listOf<T>()
        set(value) {
            Thread({ persistenceManager.writeData(value, file) }).start()
            field = value
        }

    init {
        this.items = persistenceManager.readData(file, type) ?: listOf()
    }

    override fun create(item: T) {
        items += item
    }

    override fun find(byId: String) = findAll().firstOrNull { it.id == byId }

    override fun findAll() = items

    override fun delete(item: T) {
        items -= item
    }

    override fun deleteAll() {
        items = listOf()
    }
}