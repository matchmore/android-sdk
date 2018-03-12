package io.matchmore.sdk.rx

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.api.ApiClient
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
abstract class RxBaseTest {

    // unfortunately we can't move that method to @BeforeClass because robolectric RuntimeEnvironment.application is still null there
    fun initAndStartUsingMainDevice() {
        init()
        MatchMore.instance.rxStartUsingMainDevice().testAndWait()
        assertEquals(1, MatchMore.instance.devices.findAll().size)
    }

    fun init() {
        if (!MatchMore.isConfigured()) {
            ApiClient.config.callbackInUIThread = false
            MatchMore.config(RuntimeEnvironment.application, SdkConfigTest.API_KEY, true)
        }
        removeSubscriptions()
        removePublications()
        removeDevices()
    }

    private fun removePublications() {
        MatchMore.instance.publications.rxDeleteAll().testAndWait()
        assertEquals(0, MatchMore.instance.publications.findAll().size)
    }

    private fun removeSubscriptions() {
        MatchMore.instance.subscriptions.rxDeleteAll().testAndWait()
        assertEquals(0, MatchMore.instance.subscriptions.findAll().size)
    }

    private fun removeDevices() {
        MatchMore.instance.devices.rxDeleteAll().testAndWait()
        assertEquals(0, MatchMore.instance.subscriptions.findAll().size)
    }

    protected fun mockLocation() {
        Shadows.shadowOf(RuntimeEnvironment.application).grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
        val locationManager = RuntimeEnvironment.application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val shadowLocationManager = Shadows.shadowOf(locationManager)
        val location = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 54.414663
            longitude = 18.625499
            time = System.currentTimeMillis()
        }
        shadowLocationManager.setLastKnownLocation(LocationManager.GPS_PROVIDER, location)
    }

    companion object {

        @BeforeClass
        @JvmStatic
        fun setUp() {
            ShadowLog.stream = System.out
        }
    }
}

fun <T> Single<T>.testAndWait(): TestObserver<T> = test().awaitDone(SdkConfigTest.TIMEOUT, TimeUnit.MILLISECONDS)

fun Completable.testAndWait(): TestObserver<Void> = test().awaitDone(SdkConfigTest.TIMEOUT, TimeUnit.MILLISECONDS)