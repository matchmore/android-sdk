package io.matchmore.sdk.store

interface CRUD<T> : AsyncCreateable<T>, AsyncReadable<T>, AsyncUpdateable<T>, AsyncDeleteable<T>, AsyncClearable<T>
interface CRD<T> : AsyncCreateable<T>, AsyncReadable<T>, AsyncDeleteable<T>, AsyncClearable<T>

interface AsyncCreateable<T> {
    fun create(item: T)
}

interface AsyncReadable<T> {
    fun find(byId: String): T?
    fun findAll(): List<T>
}

interface AsyncUpdateable<T> {
    fun update(item: T)
}

interface AsyncDeleteable<T> {
    fun delete(item: T)
}

interface AsyncClearable<T> {
    fun deleteAll()
}
