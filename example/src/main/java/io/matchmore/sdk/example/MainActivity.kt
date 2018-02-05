package io.matchmore.sdk.example

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.MatchMoreSdk
import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Subscription


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MatchMore.instance.apply {
            startUsingMainDevice(
                    { device ->
                        Log.i(TAG, "start using device ${device.name}")

                        val publication = Publication("Test Topic", 1.0, 0.0)
                        publication.properties = hashMapOf("test" to "true")
                        createPublication(publication, { result ->
                            Log.i(TAG, "Publication created ${result.topic}")
                        }, Throwable::printStackTrace)

                        matchMonitor.addOnMatchListener { matches, _ ->
                            Log.i(TAG, "Matches found: ${matches.size}")
                        }
                        createPollingSubscription()
                        checkLocationPermission()
                    }, Throwable::printStackTrace)
        }
    }

    override fun onResume() {
        super.onResume()
        MatchMore.instance.matchMonitor.startPollingMatches()
    }

    override fun onPause() {
        super.onPause()
        MatchMore.instance.matchMonitor.stopPollingMatches()
    }

    override fun onDestroy() {
        super.onDestroy()
        MatchMore.instance.apply {
            stopRanging()
            stopUpdatingLocation()
        }
    }

    private fun MatchMoreSdk.createPollingSubscription() {
        val subscription = Subscription("Test Topic", 1.0, 0.0)
        subscription.selector = "test = 'true'"
        createSubscription(subscription, { result ->
            Log.i(TAG, "Subscription created ${result.topic}")
        }, Throwable::printStackTrace)
    }

    private fun checkLocationPermission() {
        val permissionListener = object : PermissionListener {
            @SuppressLint("MissingPermission")
            override fun onPermissionGranted() {
                MatchMore.instance.apply {
                    startUpdatingLocation()
                    startRanging()
                }
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                Toast.makeText(this@MainActivity, R.string.if_you_reject, Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage(R.string.if_you_reject)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check()
    }

    companion object {
        private const val TAG = "MatchMore"
    }
}
