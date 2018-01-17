package io.matchmore.sdk.rx

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.BuildConfig
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.MatchMoreConfig
import net.jodah.concurrentunit.Waiter
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
abstract class RxBaseTest {

    // unfortunately we can't move that method to @BeforeClass because robolectric RuntimeEnvironment.application is still null there
    fun initAndStartUsingMainDevice() {
        init()
        MatchMore.instance.rxStartUsingMainDevice().subscribe({ _ ->
            waiter.assertEquals(1, MatchMore.instance.devices.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }

    fun init() {
        if (!MatchMore.isConfigured()) {
            MatchMore.config(MatchMoreConfig(
                    RuntimeEnvironment.application,
                    SdkConfigTest.API_KEY, SdkConfigTest.WORLD_ID,
                    callbackInUIThread = false,
                    debugLog = true))
        }
        removeSubscriptions()
        removePublications()
        removeDevices()
    }

    private fun removePublications() {
        MatchMore.instance.publications.rxDeleteAll().subscribe({
            waiter.assertEquals(0, MatchMore.instance.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }

    private fun removeSubscriptions() {
        MatchMore.instance.subscriptions.rxDeleteAll().subscribe({
            waiter.assertEquals(0, MatchMore.instance.subscriptions.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }

    private fun removeDevices() {
        MatchMore.instance.devices.rxDeleteAll().subscribe({
            waiter.assertEquals(0, MatchMore.instance.devices.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
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
        val waiter = Waiter()

        @BeforeClass
        @JvmStatic
        fun setUp() {
            ShadowLog.stream = System.out
        }
    }
}