package io.matchmore.sdk;

import net.jodah.concurrentunit.Waiter;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeoutException;

import io.matchmore.config.SdkConfigTest;
import io.matchmore.sdk.api.models.MatchmoreLocation;
import io.matchmore.sdk.api.models.Publication;
import io.matchmore.sdk.api.models.Subscription;
import io.matchmore.sdk.managers.LocationSender;
import io.matchmore.sdk.managers.MatchmoreLocationProvider;
import kotlin.Unit;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MatchesUsingCustomProviderTestJava extends BaseTestJava {
    private Waiter waiter = new Waiter();

    @Before
    public void setUp() {
        configure();
    }

    @Test
    public void gettingMatches() throws TimeoutException {
        MatchmoreSDK matchmore = Matchmore.getInstance();

        matchmore.startUsingMainDevice(device -> {
            waiter.resume();
            return Unit.INSTANCE;
        }, e -> {
            waiter.fail(e);
            return Unit.INSTANCE;
        });
        waiter.await(SdkConfigTest.TIMEOUT);

        Publication publication = new Publication("Test Topic", 20d, 100000d);
        matchmore.createPublicationForMainDevice(publication,
                device -> {
                    waiter.resume();
                    return Unit.INSTANCE;
                }, e -> {
                    waiter.fail(e);
                    return Unit.INSTANCE;
                });
        waiter.await(SdkConfigTest.TIMEOUT);

        Subscription subscription = new Subscription("Test Topic", 20d, 100000d, "");
        matchmore.createSubscriptionForMainDevice(subscription,
                device -> {
                    waiter.resume();
                    return Unit.INSTANCE;
                }, e -> {
                    waiter.fail(e);
                    return Unit.INSTANCE;
                });
        waiter.await(SdkConfigTest.TIMEOUT);
        MatchmoreLocationProvider locationProvider = new MatchmoreLocationProvider() {
            @Override
            public void startUpdatingLocation(@NotNull LocationSender sender) {
                sender.sendLocation(new MatchmoreLocation(80.0, 80.0));
            }

            @Override
            public void stopUpdatingLocation() {

            }
        };
        matchmore.startUpdatingLocation(locationProvider);
        
        // Start getting matches
        matchmore.getMatchMonitor().addOnMatchListener((matches, device) -> {
            waiter.assertTrue(matches.size() > 0);
            waiter.resume();
            return Unit.INSTANCE;
        });
        matchmore.stopUpdatingLocation();
    }
}