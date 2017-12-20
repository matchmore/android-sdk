package io.matchmore.sdk.utils

import java.util.*

interface Expirable {
    val duration: Double?
    val createdAt: Long?
    val isExpired: Boolean
        get() {
            val duration = this.duration
            val createdAt = this.createdAt
            if (duration == null || createdAt == null) return true
            val nowTimeInterval = Date().time
            return (duration * 1000).toLong() < (nowTimeInterval - createdAt)
        }
}

fun <T> List<T>.withoutExpired(): List<T> where T: Expirable = this.filter { !it.isExpired }