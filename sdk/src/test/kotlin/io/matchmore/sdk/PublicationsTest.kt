package io.matchmore.sdk

import io.matchmore.config.SdkConfigTest
import io.matchmore.sdk.api.models.Publication
import org.junit.Test

class PublicationsTest : BaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance
        val publication = Publication("Test Topic", 20.0, 100000.0)
        matchMoreSdk.createPublication(publication, { _ ->
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.createPublication(publication, { _ ->
            waiter.assertEquals(2, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.publications.delete(matchMoreSdk.publications.findAll()[0], {
            waiter.assertEquals(1, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)

        matchMoreSdk.publications.deleteAll({
            waiter.assertEquals(0, matchMoreSdk.publications.findAll().size)
            waiter.resume()
        }, waiter::fail)
        waiter.await(SdkConfigTest.TIMEOUT)
    }
}