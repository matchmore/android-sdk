package io.matchmore.sdk;

import net.jodah.concurrentunit.Waiter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.concurrent.TimeoutException;

import io.matchmore.sdk.api.models.Publication;
import io.matchmore.sdk.api.models.Subscription;
import kotlin.Unit;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DevicesTestJava {

    private Waiter waiter = new Waiter();

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
        if (!MatchMore.isConfigured()) {
            MatchMore.config(new MatchMoreConfig(
                    RuntimeEnvironment.application,
                    SdkConfigTest.API_KEY,
                    SdkConfigTest.WORLD_ID,
                    false,
                    true));
        }
    }

    @Test
    public void creations() throws TimeoutException {
        MatchMoreSdk matchMore = MatchMore.getInstance();
        matchMore.startUsingMainDevice(device -> {
            waiter.resume();
            return Unit.INSTANCE;
        }, e -> {
            waiter.fail(e);
            return Unit.INSTANCE;
        });
        waiter.await(SdkConfigTest.TIMEOUT);

        Publication publication = new Publication("Test Topic", 20d, 100000d);
        matchMore.createPublication(publication,
                device -> {
                    waiter.resume();
                    return Unit.INSTANCE;
                }, e -> {
                    waiter.fail(e);
                    return Unit.INSTANCE;
                });
        waiter.await(SdkConfigTest.TIMEOUT);

        Subscription subscription = new Subscription("Test Topic", 20d, 100000d, "");
        matchMore.createSubscription(subscription,
                device -> {
                    waiter.resume();
                    return Unit.INSTANCE;
                }, e -> {
                    waiter.fail(e);
                    return Unit.INSTANCE;
                });
        waiter.await(SdkConfigTest.TIMEOUT);
    }
}