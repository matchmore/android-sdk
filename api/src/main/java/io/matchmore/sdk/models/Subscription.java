/*
 * MATCHMORE ALPS Core REST API
 * ## ALPS by [MATCHMORE](https://dev.matchmore.io)  The first version of the MATCHMORE API is an exciting step to allow developers use a context-aware pub/sub cloud service.  A lot of mobile applications and their use cases may be modeled using this approach and can therefore profit from using MATCHMORE as their backend service.  **Build something great with [ALPS by MATCHMORE](https://dev.matchmore.io)!**   Once you've [registered your client](https://dev.matchmore.io/account/register/) it's easy start using our awesome cloud based context-aware pub/sub (admitted, a lot of buzzwords).  ## RESTful API We do our best to have all our URLs be [RESTful](https://en.wikipedia.org/wiki/Representational_state_transfer). Every endpoint (URL) may support one of four different http verbs. GET requests fetch information about an object, POST requests create objects, PUT requests update objects, and finally DELETE requests will delete objects.  ## Domain Model  This is the current domain model extended by an ontology of devices and separation between the developer portal and the ALPS Core.      +-----------+    +-------------+     | Developer +----+ Application |     +-----------+    +------+------+                             |                        \"Developer Portal\"     ........................+..........................................                             |                        \"ALPS Core\"                         +---+---+                         | World |                         +---+---+                             |                           +-------------+                             |                     +-----+ Publication |                             |                     |     +------+------+                             |                     |            |                             |                     |            |                             |                     |            |                             |                     |        +---+---+                        +----+---+-----------------+        | Match |                        | Device |                          +---+---+                        +----+---+-----------------+            |                             |                     |            |                             |                     |            |                             |                     |     +------+-------+             +---------------+--------------+      +-----+ Subscription |             |               |              |            +--------------+        +----+---+      +----+----+    +----+---+        |   Pin  |      | iBeacon |    | Mobile |        +----+---+      +---------+    +----+---+             |                              |             |         +----------+         |             +---------+ Location +---------+                       +----------+  1.  A **developer** is a mobile application developer registered in the     developer portal and allowed to use the **ALPS Developer Portal**.  A     developer might register one or more **applications** to use the     **ALPS Core cloud service**.  For developer/application pair a new     **world** is created in the **ALPS Core** and assigned an **API key** to     enable access to the ALPS Core cloud service **RESTful API**.  During     the registration, the developer needs to provide additional     configuration information for each application, e.g. its default     **push endpoint** URI for match delivery, etc. 2.  A [**device**](#tag/device) might be either *virtual* like a **pin device** or     *physical* like a **mobile device** or **iBeacon device**.  A [**pin     device**](#tag/device) is one that has geographical [**location**](#tag/location) associated with it     but is not represented by any object in the physical world; usually     it's location doesn't change frequently if at all.  A [**mobile     device**](#tag/device) is one that potentially moves together with its user and     therefore has a geographical location associated with it.  A mobile     device is typically a location-aware smartphone, which knows its     location thanks to GPS or to some other means like cell tower     triangulation, etc.  An [**iBeacon device**](#tag/device) represents an Apple     conform [iBeacon](https://developer.apple.com/ibeacon/) announcing its presence via Bluetooth LE     advertising packets which can be detected by a other mobile device.     It doesn't necessary has any location associated with it but it     serves to detect and announce its proximity to other **mobile     devices**. 3.  The hardware and software stack running on a given device is known     as its **platform**.  This include its hardware-related capabilities,     its operating systems, as well as the set of libraries (APIs)     offered to developers in order to program it. 4.  A devices may issue publications and subscriptions     at **any time**; it may also cancel publications and subscriptions     issued previously.  **Publications** and **subscriptions** do have a     definable, finite duration, after which they are deleted from the     ALPS Core cloud service and don't participate anymore in the     matching process. 5.  A [**publication**](#tag/publication) is similar to a Java Messaging Service (JMS)     publication extended with the notion of a **geographical zone**.  The     zone is defined as **circle** with a center at the given location and     a range around that location. 6.  A [**subscription**](#tag/subscription) is similar to a JMS subscription extended with the     notion of **geographical zone**. Again, the zone being defined as     **circle** with a center at the given location and a range around     that location. 7.  **Publications** and **subscriptions** which are associated with a     **mobile device**, e.g. user's mobile phone, potentially **follow the     movements** of the user carrying the device and therefore change     their associated location. 8.  A [**match**](#tag/match) between a publication and a subscription occurs when both     of the following two conditions hold:     1.  There is a **context match** occurs when for instance the         subscription zone overlaps with the publication zone or a         **proximity event** with an iBeacon device within the defined         range occurred.     2.  There is a **content match**: the publication and the subscription         match with respect to their JMS counterparts, i.e., they were         issued on the same topic and have compatible properties and the         evaluation of the selector against those properties returns true         value. 9.  A **push notification** is an asynchronous mechanism that allows an     application to receive matches for a subscription on his/her device.     Such a mechanism is clearly dependent on the device’s platform and     capabilities.  In order to use push notifications, an application must     first register a device (and possibly an application on that     device) with the ALPS core cloud service. 10. Whenever a **match** between a publication and a subscription     occurs, the device which owns the subscription receives that match     *asynchronously* via a push notification if there exists a     registered **push endpoint**.  A **push endpoint** is an URI which is     able to consume the matches for a particular device and     subscription.  The **push endpoint** doesn't necessary point to a     **mobile device** but is rather a very flexible mechanism to define     where the matches should be delivered. 11. Matches can also be retrieved by issuing a API call for a     particular device.   <a id=\"orgae4fb18\"></a>  ## Device Types                     +----+---+                    | Device |                    +--------+                    | id     |                    | name   |                    | group  |                    +----+---+                         |         +---------------+----------------+         |               |                |     +---+---+   +-------+------+    +----+-----+     |  Pin  |   | iBeacon      |    | Mobile   |     +---+---+   +--------------+    +----------+         |       | proximityUUID|    | platform |         |       | major        |    | token    |         |       | minor        |    +----+-----+         |       +-------+------+         |         |               |                |         |               | <--???         |         |          +----+-----+          |         +----------+ Location +----------+                    +----------+   <a id=\"org68cc0d8\"></a>  ### Generic `Device`  -   id -   name -   group  <a id=\"orgc430925\"></a>  ### `PinDevice`  -   location   <a id=\"orgecaed9f\"></a>  ### `iBeaconDevice`  -   proximityUUID -   major -   minor   <a id=\"org7b09b62\"></a>  ### `MobileDevice`  -   platform -   deviceToken -   location 
 *
 * OpenAPI spec version: 0.5.0
 * Contact: apihelp@matchmore.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.matchmore.sdk.models;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A subscription can be seen as a JMS subscription extended with the notion of geographical zone. The zone again being defined as circle with a center at the given location and a range around that location. 
 */
@ApiModel(description = "A subscription can be seen as a JMS subscription extended with the notion of geographical zone. The zone again being defined as circle with a center at the given location and a range around that location. ")

public class Subscription {
  @SerializedName("id")
  private String id = null;

  @SerializedName("createdAt")
  private Long createdAt = null;

  @SerializedName("worldId")
  private String worldId = null;

  @SerializedName("deviceId")
  private String deviceId = null;

  @SerializedName("topic")
  private String topic = null;

  @SerializedName("selector")
  private String selector = null;

  @SerializedName("range")
  private Double range = null;

  @SerializedName("duration")
  private Double duration = null;

  @SerializedName("pushers")
  private List<String> pushers = null;

   /**
   * The id (UUID) of the subscription.
   * @return id
  **/
  @ApiModelProperty(required = true, value = "The id (UUID) of the subscription.")
  public String getId() {
    return id;
  }

   /**
   * The timestamp of the subscription creation in seconds since Jan 01 1970 (UTC). 
   * @return createdAt
  **/
  @ApiModelProperty(required = true, value = "The timestamp of the subscription creation in seconds since Jan 01 1970 (UTC). ")
  public Long getCreatedAt() {
    return createdAt;
  }

  public Subscription worldId(String worldId) {
    this.worldId = worldId;
    return this;
  }

   /**
   * The id (UUID) of the world that contains device to attach a subscription to.
   * @return worldId
  **/
  @ApiModelProperty(required = true, value = "The id (UUID) of the world that contains device to attach a subscription to.")
  public String getWorldId() {
    return worldId;
  }

  public void setWorldId(String worldId) {
    this.worldId = worldId;
  }

  public Subscription deviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

   /**
   * The id (UUID) of the device to attach a subscription to.
   * @return deviceId
  **/
  @ApiModelProperty(required = true, value = "The id (UUID) of the device to attach a subscription to.")
  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public Subscription topic(String topic) {
    this.topic = topic;
    return this;
  }

   /**
   * The topic of the subscription. This will act as a first match filter. For a subscription to be able to match a publication they must have the exact same topic. 
   * @return topic
  **/
  @ApiModelProperty(required = true, value = "The topic of the subscription. This will act as a first match filter. For a subscription to be able to match a publication they must have the exact same topic. ")
  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public Subscription selector(String selector) {
    this.selector = selector;
    return this;
  }

   /**
   * This is an expression to filter the publications. For instance &#39;job&#x3D;&#39;developer&#39;&#39; will allow matching only with publications containing a &#39;job&#39; key with a value of &#39;developer&#39;. 
   * @return selector
  **/
  @ApiModelProperty(required = true, value = "This is an expression to filter the publications. For instance 'job='developer'' will allow matching only with publications containing a 'job' key with a value of 'developer'. ")
  public String getSelector() {
    return selector;
  }

  public void setSelector(String selector) {
    this.selector = selector;
  }

  public Subscription range(Double range) {
    this.range = range;
    return this;
  }

   /**
   * The range of the subscription in meters. This is the range around the device holding the subscription in which matches with publications can be triggered. 
   * @return range
  **/
  @ApiModelProperty(required = true, value = "The range of the subscription in meters. This is the range around the device holding the subscription in which matches with publications can be triggered. ")
  public Double getRange() {
    return range;
  }

  public void setRange(Double range) {
    this.range = range;
  }

  public Subscription duration(Double duration) {
    this.duration = duration;
    return this;
  }

   /**
   * The duration of the subscription in seconds. If set to &#39;-1&#39; the subscription will live forever and if set to &#39;0&#39; it will be instant at the time of subscription. 
   * @return duration
  **/
  @ApiModelProperty(required = true, value = "The duration of the subscription in seconds. If set to '-1' the subscription will live forever and if set to '0' it will be instant at the time of subscription. ")
  public Double getDuration() {
    return duration;
  }

  public void setDuration(Double duration) {
    this.duration = duration;
  }

  public Subscription pushers(List<String> pushers) {
    this.pushers = pushers;
    return this;
  }

  public Subscription addPushersItem(String pushersItem) {
    if (this.pushers == null) {
      this.pushers = new ArrayList<String>();
    }
    this.pushers.add(pushersItem);
    return this;
  }

   /**
   * When match will occurs, they will be notified on these provided URI(s) address(es) in the pushers array. 
   * @return pushers
  **/
  @ApiModelProperty(value = "When match will occurs, they will be notified on these provided URI(s) address(es) in the pushers array. ")
  public List<String> getPushers() {
    return pushers;
  }

  public void setPushers(List<String> pushers) {
    this.pushers = pushers;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Subscription subscription = (Subscription) o;
    return Objects.equals(this.id, subscription.id) &&
        Objects.equals(this.createdAt, subscription.createdAt) &&
        Objects.equals(this.worldId, subscription.worldId) &&
        Objects.equals(this.deviceId, subscription.deviceId) &&
        Objects.equals(this.topic, subscription.topic) &&
        Objects.equals(this.selector, subscription.selector) &&
        Objects.equals(this.range, subscription.range) &&
        Objects.equals(this.duration, subscription.duration) &&
        Objects.equals(this.pushers, subscription.pushers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdAt, worldId, deviceId, topic, selector, range, duration, pushers);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Subscription {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    worldId: ").append(toIndentedString(worldId)).append("\n");
    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    topic: ").append(toIndentedString(topic)).append("\n");
    sb.append("    selector: ").append(toIndentedString(selector)).append("\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    pushers: ").append(toIndentedString(pushers)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

