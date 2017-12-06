package io.matchmore.sdk;

import io.matchmore.CollectionFormats.*;



import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;

import io.matchmore.sdk.models.APIError;
import io.matchmore.sdk.models.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface LocationApi {
  /**
   * Create a new location for a device
   * 
   * @param deviceId The id (UUID) of the device. (required)
   * @param location Location to create for a device.  (required)
   * @return Call&lt;Location&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("devices/{deviceId}/locations")
  Call<Location> createLocation(
    @retrofit2.http.Path("deviceId") String deviceId, @retrofit2.http.Body Location location
  );

}
