package io.matchmore.sdk.api

import io.matchmore.sdk.api.models.MatchmoreLocation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


interface LocationApi {
    /**
     * Create a new location for a device
     *
     * @param deviceId The id (UUID) of the device. (required)
     * @param location Location to create for a device.  (required)
     * @return Call&lt;Location&gt;
     */
    @Headers("Content-Type:application/json")
    @POST("devices/{deviceId}/locations")
    fun createLocation(
            @Path("deviceId") deviceId: String, @Body location: MatchmoreLocation
    ): Call<MatchmoreLocation>

}
