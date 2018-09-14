package io.matchmore.sdk.store

import io.matchmore.sdk.api.models.HasId
import io.matchmore.sdk.utils.PersistenceManager

open class Store<T : HasId>(
        private val persistenceManager: PersistenceManager,
        private val file: String) {

    open var items = listOf<T>()
        set(value) {
            Thread { persistenceManager.writeData(value, file) }.start()
            field = value
        }

    fun find(byId: String) = findAll().firstOrNull { it.id == byId }

    fun findAll() = items

    protected fun createData(item: T) {
        items += item
    }

    protected fun deleteData(item: T) {
        items -= item
    }

    protected fun deleteAllData() {
        items = listOf()
    }
}