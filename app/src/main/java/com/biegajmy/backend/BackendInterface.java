package com.biegajmy.backend;

import com.biegajmy.model.Event;
import com.biegajmy.model.NewEvent;
import com.biegajmy.model.User;
import java.util.List;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface BackendInterface {

    @POST("/event") Response createEvent(@Query("token") String token, @Body NewEvent event)
        throws BackendError;

    @GET("/event") List<Event> listEvents(@Query("x") double x, @Query("y") double y,
        @Query("max") int max, @Query("token") String token) throws BackendError;

    @PUT("/event/{event_id}/user") Event joinEvent(@Path("event_id") String eventId,
        @Query("token") String token) throws BackendError;

    @DELETE("/event/{event_id}/user") Event leaveEvent(@Path("event_id") String eventId,
        @Query("token") String token) throws BackendError;

    @POST("/user") Response createUser(@Query("token") String token, @Body User user)
        throws BackendError;
}
