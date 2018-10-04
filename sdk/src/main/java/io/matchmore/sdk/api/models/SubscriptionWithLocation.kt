package io.matchmore.sdk.api.models

import com.google.gson.annotations.SerializedName
import io.matchmore.sdk.utils.Expirable
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * A subscription can be seen as a JMS subscription extended with the notion of geographical zone. The zone again being defined as circle with a center at the given location and a range around that location.
 */
@ApiModel(description = "A subscription can be seen as a JMS subscription extended with the notion of geographical zone. The zone again being defined as circle with a center at the given location and a range around that location. ")
data class SubscriptionWithLocation @JvmOverloads constructor(
        /**
         * The id (UUID) of the subscription.
         * @return id
         */
        @SerializedName("id")
        @get:ApiModelProperty(value = "The id (UUID) of the subscription.")
        override val id: String? = null,

        /**
         * The timestamp of the subscription creation in seconds since Jan 01 1970 (UTC).
         * @return createdAt
         */
        @SerializedName("createdAt")
        @get:ApiModelProperty(value = "The timestamp of the subscription creation in seconds since Jan 01 1970 (UTC). ")
        override val createdAt: Long? = null,

        /**
         * The id (UUID) of the world that contains device to attach a subscription to.
         * @return worldId
         */
        @SerializedName("worldId")
        @get:ApiModelProperty(required = true, value = "The id (UUID) of the world that contains device to attach a subscription to.")
        var worldId: String? = null,

        /**
         * The id (UUID) of the device to attach a subscription to.
         * @return deviceId
         */
        @SerializedName("deviceId")
        @get:ApiModelProperty(required = true, value = "The id (UUID) of the device to attach a subscription to.")
        var deviceId: String? = null,

        /**
         * The topic of the subscription. This will act as a first match filter. For a subscription to be able to match a publication they must have the exact same topic.
         * @return topic
         */
        @SerializedName("topic")
        @get:ApiModelProperty(required = true, value = "The topic of the subscription. This will act as a first match filter. For a subscription to be able to match a publication they must have the exact same topic. ")
        var topic: String? = null,

        /**
         * This is an expression to filter the publications. For instance &#39;job&#x3D;&#39;developer&#39;&#39; will allow matching only with publications containing a &#39;job&#39; key with a value of &#39;developer&#39;.
         * @return selector
         */
        @SerializedName("selector")
        @get:ApiModelProperty(required = true, value = "This is an expression to filter the publications. For instance 'job='developer'' will allow matching only with publications containing a 'job' key with a value of 'developer'. ")
        var selector: String? = null,

        /**
         * Get location
         * @return location
         */
        @SerializedName("location")
        @get:ApiModelProperty(required = true, value = "")
        var location: MatchmoreLocation? = null,

        /**
         * The range of the subscription in meters. This is the range around the device holding the subscription in which matches with publications can be triggered.
         * @return range
         */
        @SerializedName("range")
        @get:ApiModelProperty(required = true, value = "The range of the subscription in meters. This is the range around the device holding the subscription in which matches with publications can be triggered. ")
        var range: Double? = null,

        /**
         * The duration of the subscription in seconds. If set to &#39;0&#39; it will be instant at the time of subscription. Negative values are not allowed.
         * @return duration
         */
        @SerializedName("duration")
        @get:ApiModelProperty(required = true, value = "The duration of the subscription in seconds. If set to '0' it will be instant at the time of subscription. Negative values are not allowed. ")
        override var duration: Double? = null,

        /**
         * When match will occurs, they will be notified on these provided URI(s) address(es) in the pushers array.
         * @return pushers
         */
        @SerializedName("pushers")
        @get:ApiModelProperty(value = "When match will occurs, they will be notified on these provided URI(s) address(es) in the pushers array. ")
        var pushers: List<String>? = null
) : HasId, Expirable

