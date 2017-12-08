package io.matchmore.sdk

import android.annotation.SuppressLint
import io.matchmore.sdk.api.ErrorCallback
import io.matchmore.sdk.api.SuccessCallback
import io.matchmore.sdk.api.models.MobileDevice

@SuppressLint("StaticFieldLeak")
object MatchMore {

    private var matchMoreConfig: MatchMoreConfig? = null

    @JvmStatic
    val instance: MatchMoreSdk by lazy {
        if (!isConfigured()) throw IllegalStateException("Please config first.")
        AlpsManager(matchMoreConfig!!)
    }

    @JvmStatic
    fun config(matchMoreConfig: MatchMoreConfig) {
        if (isConfigured()) throw IllegalStateException("You can not overwrite the configuration.")
        this.matchMoreConfig = matchMoreConfig
    }

    @JvmStatic
    fun isConfigured() = this.matchMoreConfig != null
}

interface MatchMoreSdk {
    fun startUsingMainDevice(success: SuccessCallback<MobileDevice>? = null, error: ErrorCallback? = null) = startUsingMainDevice(null, success, error)
    fun startUsingMainDevice(device: MobileDevice? = null, success: SuccessCallback<MobileDevice>? = null, error: ErrorCallback? = null)
}