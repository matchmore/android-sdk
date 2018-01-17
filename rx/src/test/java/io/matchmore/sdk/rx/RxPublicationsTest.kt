package io.matchmore.sdk.rx

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.api.models.Publication
import org.junit.Test

class RxPublicationsTest : RxBaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance
        val publication = Publication("Test Topic", 20.0, 100000.0)
        matchMoreSdk.rxCreatePublication(publication).subscribe({ _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.rxCreatePublication(publication).subscribe({ _ ->
            waiter.assertEquals(2, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.publications.rxDelete(matchMoreSdk.publications.findAll()[0]).subscribe({
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.publications.rxDeleteAll().subscribe({
            waiter.assertEquals(0, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}