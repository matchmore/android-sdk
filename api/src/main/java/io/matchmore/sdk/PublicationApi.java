package io.matchmore.sdk;

import io.matchmore.CollectionFormats.*;



import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;

import io.matchmore.sdk.models.APIError;
import io.matchmore.sdk.models.Publication;
import io.matchmore.sdk.models.Publications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface PublicationApi {
  /**
   * Create a publication for a device
   * 
   * @param deviceId The id (UUID) of the device. (required)
   * @param publication Publication to create on a device.  (required)
   * @return Call&lt;Publication&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("devices/{deviceId}/publications")
  Call<Publication> createPublication(
    @retrofit2.http.Path("deviceId") String deviceId, @retrofit2.http.Body Publication publication
  );

  /**
   * Delete a Publication
   * 
   * @param deviceId The id (UUID) of the device. (required)
   * @param publicationId The id (UUID) of the subscription. (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @DELETE("devices/{deviceId}/publications/{publicationId}")
  Call<Void> deletePublication(
    @retrofit2.http.Path("deviceId") String deviceId, @retrofit2.http.Path("publicationId") String publicationId
  );

  /**
   * Info about a publication on a device
   * 
   * @param deviceId The id (UUID) of the device. (required)
   * @param publicationId The id (UUID) of the publication. (required)
   * @return Call&lt;Publication&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("devices/{deviceId}/publications/{publicationId}")
  Call<Publication> getPublication(
    @retrofit2.http.Path("deviceId") String deviceId, @retrofit2.http.Path("publicationId") String publicationId
  );

  /**
   * Get all publications for a device
   * 
   * @param deviceId The id (UUID) of the device. (required)
   * @return Call&lt;Publications&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @GET("devices/{deviceId}/publications")
  Call<Publications> getPublications(
    @retrofit2.http.Path("deviceId") String deviceId
  );

}
