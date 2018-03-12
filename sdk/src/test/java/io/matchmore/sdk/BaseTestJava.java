package io.matchmore.sdk;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLog;

import io.matchmore.config.SdkConfigTest;
import io.matchmore.sdk.api.ApiClient;

 class BaseTestJava {
    void configure() {
        ShadowLog.stream = System.out;
        if (!MatchMore.isConfigured()) {
            ApiClient.getConfig().setCallbackInUIThread(false);
            MatchMore.config(RuntimeEnvironment.application, SdkConfigTest.API_KEY, true);
        }
    }
}
