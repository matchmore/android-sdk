package io.matchmore.sdk.api


import io.matchmore.sdk.api.models.Matches
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


interface MatchesApi {
    /**
     * Get matches for the device
     *
     * @param deviceId The id (UUID) of the device. (required)
     * @return Call&lt;Matches&gt;
     */
    @Headers("Content-Type:application/json")
    @GET("devices/{deviceId}/matches")
    fun getMatches(
            @Path("deviceId") deviceId: String
    ): Call<Matches>

}
