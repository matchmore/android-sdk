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

import kotlin.Unit;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
class DevicesTestJava {

    private Waiter waiter = new Waiter();

    @Before
    void setUp() {
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
    void createMainDevice() throws TimeoutException {
        MatchMore.getInstance().startUsingMainDevice(device -> {
            waiter.resume();
            return Unit.INSTANCE;
        }, e -> {
            waiter.fail(e);
            return Unit.INSTANCE;
        });
        waiter.await(SdkConfigTest.TIMEOUT);
    }
}