package io.matchmore.sdk.api


import io.matchmore.sdk.api.models.Subscription
import io.matchmore.sdk.api.models.Subscriptions
import retrofit2.Call
import retrofit2.http.*


interface SubscriptionApi {
    /**
     * Create a subscription for a device
     *
     * @param deviceId The id (UUID) of the device.  (required)
     * @param subscription Subscription to create on a device.  (required)
     * @return Call&lt;Subscription&gt;
     */
    @Headers("Content-Type:application/json")
    @POST("devices/{deviceId}/subscriptions")
    fun createSubscription(
            @Path("deviceId") deviceId: String, @Body subscription: Subscription
    ): Call<Subscription>

    /**
     * Delete a Subscription
     *
     * @param deviceId The id (UUID) of the device. (required)
     * @param subscriptionId The id (UUID) of the subscription. (required)
     * @return Call&lt;Void&gt;
     */
    @Headers("Content-Type:application/json")
    @DELETE("devices/{deviceId}/subscriptions/{subscriptionId}")
    fun deleteSubscription(
            @Path("deviceId") deviceId: String, @Path("subscriptionId") subscriptionId: String
    ): Call<Void>

    /**
     * Info about a subscription on a device
     *
     * @param deviceId The id (UUID) of the device. (required)
     * @param subscriptionId The id (UUID) of the subscription. (required)
     * @return Call&lt;Subscription&gt;
     */
    @Headers("Content-Type:application/json")
    @GET("devices/{deviceId}/subscriptions/{subscriptionId}")
    fun getSubscription(
            @Path("deviceId") deviceId: String, @Path("subscriptionId") subscriptionId: String
    ): Call<Subscription>

    /**
     * Get all subscriptions for a device
     *
     * @param deviceId The id (UUID) of the device. (required)
     * @return Call&lt;Subscriptions&gt;
     */
    @Headers("Content-Type:application/json")
    @GET("devices/{deviceId}/subscriptions")
    fun getSubscriptions(
            @Path("deviceId") deviceId: String
    ): Call<Subscriptions>

}
