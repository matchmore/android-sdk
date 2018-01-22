package io.matchmore.sdk.rx

import io.matchmore.sdk.MatchMore
import io.matchmore.sdk.api.models.Publication
import junit.framework.Assert.assertEquals
import org.junit.Test

class RxPublicationsTest : RxBaseTest() {

    @Test
    fun test() {
        initAndStartUsingMainDevice()

        val matchMoreSdk = MatchMore.instance
        val publication = Publication("Test Topic", 20.0, 100000.0)
        matchMoreSdk.rxCreatePublication(publication).testAndWait()
        assertEquals(1, matchMoreSdk.publications.findAll().size)

        matchMoreSdk.rxCreatePublication(publication).testAndWait()
        assertEquals(2, matchMoreSdk.publications.findAll().size)

        matchMoreSdk.publications.rxDelete(matchMoreSdk.publications.findAll()[0]).testAndWait()
        assertEquals(1, matchMoreSdk.publications.findAll().size)

        matchMoreSdk.publications.rxDeleteAll().testAndWait()
        assertEquals(0, matchMoreSdk.publications.findAll().size)
    }
}