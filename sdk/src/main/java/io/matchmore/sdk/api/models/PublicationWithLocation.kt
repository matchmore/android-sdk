package io.matchmore.sdk.api.models

import com.google.gson.annotations.SerializedName
import io.matchmore.sdk.utils.Expirable
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 * A publication can be seen as a JavaMessagingService (JMS) publication extended with the notion of a geographical zone. The zone is defined as circle with a center at the given location and a range around that location.
 */
@ApiModel(description = "A publication can be seen as a JavaMessagingService (JMS) publication extended with the notion of a geographical zone. The zone is defined as circle with a center at the given location and a range around that location. ")
data class PublicationWithLocation @JvmOverloads constructor(
        /**
         * The id (UUID) of the publication.
         * @return id
         */
        @SerializedName("id")
        @get:ApiModelProperty(value = "The id (UUID) of the publication.")
        override val id: String? = null,

        /**
         * The timestamp of the publication creation in seconds since Jan 01 1970 (UTC).
         * @return createdAt
         */
        @SerializedName("createdAt")
        @get:ApiModelProperty(value = "The timestamp of the publication creation in seconds since Jan 01 1970 (UTC). ")
        override val createdAt: Long? = null,

        /**
         * The id (UUID) of the world that contains device to attach a publication to.
         * @return worldId
         */
        @SerializedName("worldId")
        @get:ApiModelProperty(required = true, value = "The id (UUID) of the world that contains device to attach a publication to.")
        var worldId: String? = null,

        /**
         * The id (UUID) of the device to attach a publication to.
         * @return deviceId
         */
        @SerializedName("deviceId")
        @get:ApiModelProperty(required = true, value = "The id (UUID) of the device to attach a publication to.")
        var deviceId: String? = null,

        /**
         * The topic of the publication. This will act as a first match filter. For a subscription to be able to match a publication they must have the exact same topic.
         * @return topic
         */
        @SerializedName("topic")
        @get:ApiModelProperty(required = true, value = "The topic of the publication. This will act as a first match filter. For a subscription to be able to match a publication they must have the exact same topic. ")
        var topic: String? = null,

        /**
         * Get location
         * @return location
         */
        @SerializedName("location")
        @get:ApiModelProperty(required = true, value = "")
        var location: MatchmoreLocation? = null,

        /**
         * The range of the publication in meters. This is the range around the device holding the publication in which matches with subscriptions can be triggered.
         * @return range
         */
        @SerializedName("range")
        @get:ApiModelProperty(required = true, value = "The range of the publication in meters. This is the range around the device holding the publication in which matches with subscriptions can be triggered. ")
        var range: Double? = null,

        /**
         * The duration of the publication in seconds. If set to &#39;0&#39; it will be instant at the time of publication. Negative values are not allowed.
         * @return duration
         */
        @SerializedName("duration")
        @get:ApiModelProperty(required = true, value = "The duration of the publication in seconds. If set to '0' it will be instant at the time of publication. Negative values are not allowed. ")
        override var duration: Double? = null,

        /**
         * The dictionary of key, value pairs. Allowed values are number, boolean, string and array of aforementioned types
         * @return properties
         */
        @SerializedName("properties")
        @get:ApiModelProperty(example = "", required = true, value = "The dictionary of key, value pairs. Allowed values are number, boolean, string and array of aforementioned types")
        var properties: MutableMap<String, Any> = HashMap()

) : HasId, Expirable

