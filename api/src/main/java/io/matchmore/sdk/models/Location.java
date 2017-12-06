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

/**
 * Location
 */

public class Location {
  @SerializedName("createdAt")
  private Long createdAt = null;

  @SerializedName("latitude")
  private Double latitude = 0.0d;

  @SerializedName("longitude")
  private Double longitude = 0.0d;

  @SerializedName("altitude")
  private Double altitude = 0.0d;

  @SerializedName("horizontalAccuracy")
  private Double horizontalAccuracy = 1.0d;

  @SerializedName("verticalAccuracy")
  private Double verticalAccuracy = 1.0d;

   /**
   * The timestamp of the location creation in seconds since Jan 01 1970 (UTC). 
   * @return createdAt
  **/
  @ApiModelProperty(required = true, value = "The timestamp of the location creation in seconds since Jan 01 1970 (UTC). ")
  public Long getCreatedAt() {
    return createdAt;
  }

  public Location latitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

   /**
   * The latitude of the device in degrees, for instance &#39;46.5333&#39; (Lausanne, Switzerland). 
   * @return latitude
  **/
  @ApiModelProperty(required = true, value = "The latitude of the device in degrees, for instance '46.5333' (Lausanne, Switzerland). ")
  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Location longitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

   /**
   * The longitude of the device in degrees, for instance &#39;6.6667&#39; (Lausanne, Switzerland). 
   * @return longitude
  **/
  @ApiModelProperty(required = true, value = "The longitude of the device in degrees, for instance '6.6667' (Lausanne, Switzerland). ")
  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Location altitude(Double altitude) {
    this.altitude = altitude;
    return this;
  }

   /**
   * The altitude of the device in meters, for instance &#39;495.0&#39; (Lausanne, Switzerland). 
   * @return altitude
  **/
  @ApiModelProperty(required = true, value = "The altitude of the device in meters, for instance '495.0' (Lausanne, Switzerland). ")
  public Double getAltitude() {
    return altitude;
  }

  public void setAltitude(Double altitude) {
    this.altitude = altitude;
  }

  public Location horizontalAccuracy(Double horizontalAccuracy) {
    this.horizontalAccuracy = horizontalAccuracy;
    return this;
  }

   /**
   * The horizontal accuracy of the location, measured on a scale from &#39;0.0&#39; to &#39;1.0&#39;, &#39;1.0&#39; being the most accurate. If this value is not specified then the default value of &#39;1.0&#39; is used. 
   * @return horizontalAccuracy
  **/
  @ApiModelProperty(value = "The horizontal accuracy of the location, measured on a scale from '0.0' to '1.0', '1.0' being the most accurate. If this value is not specified then the default value of '1.0' is used. ")
  public Double getHorizontalAccuracy() {
    return horizontalAccuracy;
  }

  public void setHorizontalAccuracy(Double horizontalAccuracy) {
    this.horizontalAccuracy = horizontalAccuracy;
  }

  public Location verticalAccuracy(Double verticalAccuracy) {
    this.verticalAccuracy = verticalAccuracy;
    return this;
  }

   /**
   * The vertical accuracy of the location, measured on a scale from &#39;0.0&#39; to &#39;1.0&#39;, &#39;1.0&#39; being the most accurate. If this value is not specified then the default value of &#39;1.0&#39; is used. 
   * @return verticalAccuracy
  **/
  @ApiModelProperty(value = "The vertical accuracy of the location, measured on a scale from '0.0' to '1.0', '1.0' being the most accurate. If this value is not specified then the default value of '1.0' is used. ")
  public Double getVerticalAccuracy() {
    return verticalAccuracy;
  }

  public void setVerticalAccuracy(Double verticalAccuracy) {
    this.verticalAccuracy = verticalAccuracy;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    return Objects.equals(this.createdAt, location.createdAt) &&
        Objects.equals(this.latitude, location.latitude) &&
        Objects.equals(this.longitude, location.longitude) &&
        Objects.equals(this.altitude, location.altitude) &&
        Objects.equals(this.horizontalAccuracy, location.horizontalAccuracy) &&
        Objects.equals(this.verticalAccuracy, location.verticalAccuracy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(createdAt, latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Location {\n");
    
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    altitude: ").append(toIndentedString(altitude)).append("\n");
    sb.append("    horizontalAccuracy: ").append(toIndentedString(horizontalAccuracy)).append("\n");
    sb.append("    verticalAccuracy: ").append(toIndentedString(verticalAccuracy)).append("\n");
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

