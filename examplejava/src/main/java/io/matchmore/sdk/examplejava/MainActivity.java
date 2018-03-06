package io.matchmore.sdk.examplejava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import io.matchmore.config.SdkConfigTest;
import io.matchmore.sdk.MatchMore;
import io.matchmore.sdk.MatchMoreConfig;
import io.matchmore.sdk.MatchMoreSdk;
import io.matchmore.sdk.api.models.Publication;
import io.matchmore.sdk.api.models.Subscription;
import kotlin.Unit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Configuration of api key/world id
        if (!MatchMore.isConfigured()) {
            MatchMore.config(new MatchMoreConfig(
                    this,
                    SdkConfigTest.API_KEY,
                    SdkConfigTest.WORLD_ID,
                    null,
                    null,
                    false,
                    true));
        }

        // Getting instance. It's static variable. It's possible to have only one instance of matchmore.
        MatchMoreSdk matchMore = MatchMore.getInstance();

        // Creating main device.
        matchMore.startUsingMainDevice(device -> {
            Publication publication = new Publication("Test Topic", 20d, 100000d);
            matchMore.createPublication(publication, createdPublication -> {
                Log.d("JavaExample", publication.getId());
                return Unit.INSTANCE;
            }, e -> {
                Log.d("JavaExample", e.getMessage());
                return Unit.INSTANCE;
            });

            Subscription subscription = new Subscription("Test Topic", 20d, 100000d, "");
            matchMore.createSubscription(subscription, createdSubscription -> {
                Log.d("JavaExample", subscription.getId());
                return Unit.INSTANCE;
            }, e -> {
                Log.d("JavaExample", e.getMessage());
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE; // this is important (b/c kotlin vs java callbacks differ in implementation)
        }, e -> {
            Log.d("JavaExample", e.getMessage());
            return Unit.INSTANCE;
        });

        matchMore.getMatchMonitor().addOnMatchListener((matches, device) -> {
            Log.d("JavaExample", device.getId());
            return Unit.INSTANCE;
        });
        matchMore.startUpdatingLocation();
    }
}
