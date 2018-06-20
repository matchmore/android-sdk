package io.matchmore.sdk;

import net.jodah.concurrentunit.Waiter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeoutException;

import io.matchmore.config.SdkConfigTest;
import io.matchmore.sdk.api.models.Publication;
import io.matchmore.sdk.api.models.Subscription;
import kotlin.Unit;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MatchesTestJava extends BaseTestJava {
    private Waiter waiter = new Waiter();
    private String removablePubId = null;

    @Before
    public void setUp() {
        configure();
    }

    @Test
    public void removingPublication() throws TimeoutException {
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
                pub -> {
                    removablePubId = pub.getId();
                    waiter.resume();
                    return Unit.INSTANCE;
                }, e -> {
                    waiter.fail(e);
                    return Unit.INSTANCE;
                });
        waiter.await(SdkConfigTest.TIMEOUT);

        Publication toBeRemovedPub = matchmore.getPublications().find(removablePubId);
        matchmore.getPublications().delete(toBeRemovedPub,
                () -> {
                    waiter.resume();
                    return Unit.INSTANCE;
                }, e -> {
                    waiter.fail(e);
                    return Unit.INSTANCE;
                });
        waiter.await(SdkConfigTest.TIMEOUT);
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
                pub -> {
                    waiter.resume();
                    return Unit.INSTANCE;
                }, e -> {
                    waiter.fail(e);
                    return Unit.INSTANCE;
                });
        waiter.await(SdkConfigTest.TIMEOUT);

        Subscription subscription = new Subscription("Test Topic", 20d, 100000d, "");
        matchmore.createSubscriptionForMainDevice(subscription,
                sub -> {
                    waiter.resume();
                    return Unit.INSTANCE;
                }, e -> {
                    waiter.fail(e);
                    return Unit.INSTANCE;
                });
        waiter.await(SdkConfigTest.TIMEOUT);

        BaseTest.mockLocation();
        matchmore.startUpdatingLocation();
        
        // Start getting matches
        matchmore.getMatchMonitor().addOnMatchListener((matches, device) -> {
            waiter.assertTrue(matches.size() >= 0);
            waiter.resume();
            return Unit.INSTANCE;
        });
    }
}