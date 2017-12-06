package io.matchmore.sdk;

import io.matchmore.CollectionFormats.*;



import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;

import io.matchmore.sdk.models.APIError;
import io.matchmore.sdk.models.Matches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface MatchesApi {
  /**
   * Get matches for the device
   * 
   * @param deviceId The id (UUID) of the device. (required)
   * @return Call&lt;Matches&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("devices/{deviceId}/matches")
  Call<Matches> getMatches(
    @retrofit2.http.Path("deviceId") String deviceId
  );

}
