package io.matchmore.sdk.api

import io.matchmore.sdk.api.models.Publication
import io.matchmore.sdk.api.models.Publications
import retrofit2.Call
import retrofit2.http.*


interface PublicationApi {
    /**
     * Create a publication for a device
     *
     * @param deviceId The id (UUID) of the device. (required)
     * @param publication Publication to create on a device.  (required)
     * @return Call&lt;Publication&gt;
     */
    @Headers("Content-Type:application/json")
    @POST("devices/{deviceId}/publications")
    fun createPublication(
            @Path("deviceId") deviceId: String, @Body publication: Publication
    ): Call<Publication>

    /**
     * Delete a Publication
     *
     * @param deviceId The id (UUID) of the device. (required)
     * @param publicationId The id (UUID) of the subscription. (required)
     * @return Call&lt;Void&gt;
     */
    @Headers("Content-Type:application/json")
    @DELETE("devices/{deviceId}/publications/{publicationId}")
    fun deletePublication(
            @Path("deviceId") deviceId: String, @Path("publicationId") publicationId: String
    ): Call<Void>

    /**
     * Info about a publication on a device
     *
     * @param deviceId The id (UUID) of the device. (required)
     * @param publicationId The id (UUID) of the publication. (required)
     * @return Call&lt;Publication&gt;
     */
    @Headers("Content-Type:application/json")
    @GET("devices/{deviceId}/publications/{publicationId}")
    fun getPublication(
            @Path("deviceId") deviceId: String, @Path("publicationId") publicationId: String
    ): Call<Publication>

    /**
     * Get all publications for a device
     *
     * @param deviceId The id (UUID) of the device. (required)
     * @return Call&lt;Publications&gt;
     */
    @Headers("Content-Type:application/json")
    @GET("devices/{deviceId}/publications")
    fun getPublications(
            @Path("deviceId") deviceId: String
    ): Call<Publications>

}
