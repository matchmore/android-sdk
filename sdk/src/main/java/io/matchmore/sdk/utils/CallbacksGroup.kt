package io.matchmore.sdk.utils

import io.matchmore.sdk.api.CompleteCallback
import io.matchmore.sdk.api.ErrorCallback

class CallbacksGroup(private val complete: CompleteCallback? = null, private val error: ErrorCallback? = null) {

    private var counter = 0

    private var lastError: Throwable? = null

    fun enter() = counter++

    fun complete() {
        counter--
        end()
    }

    fun error(throwable: Throwable) {
        lastError = throwable
    }

    fun end() {
        if (counter == 0) {
            if (lastError != null) error?.invoke(lastError!!) else complete?.invoke()
        }
    }
}