package com.biegajmy.backend;

import com.biegajmy.model.CommentList;
import com.biegajmy.model.Event;
import com.biegajmy.model.NewEvent;
import com.biegajmy.model.Token;
import com.biegajmy.model.User;
import java.util.List;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public interface BackendInterface {

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @POST("/event") Response createEvent(@Query("token") String token, @Body NewEvent event) throws BackendError;

    @PUT("/event/{event_id}") Event updateEvent(@Path("event_id") String eventId, @Body NewEvent event,
        @Query("token") String token);

    @DELETE("/event/{event_id}") Response deleteEvent(@Path("event_id") String eventId, @Query("token") String token)
        throws BackendError;

    @GET("/event") List<Event> listEvents(@Query("x") double x, @Query("y") double y, @Query("max") int max)
        throws BackendError;

    @PUT("/event/{event_id}/user") Event joinEvent(@Path("event_id") String eventId, @Query("token") String token)
        throws BackendError;

    @DELETE("/event/{event_id}/user") Event leaveEvent(@Path("event_id") String eventId, @Query("token") String token)
        throws BackendError;

    @PUT("/event/{event_id}/comment") CommentList comment(@Path("event_id") String eventId, @Query("msg") String msg,
        @Query("token") String token) throws BackendError;

    //********************************************************************************************//
    // User
    //********************************************************************************************//

    @PUT("/user/{user_id}") User updateUser(@Path("user_id") String userId, @Query("token") String token,
        @Body User user) throws BackendError;

    @Multipart @PUT("/user/{user_id}/photo") void updatePhoto(@Path("user_id") String userId,
        @Query("token") String token, @Part("photo") TypedFile file, Callback<User> cb);

    @GET("/user/{user_id}") User getUser(@Path("user_id") String userId, @Query("token") String token)
        throws BackendError;

    @GET("/user/{user_id}/events") List<Event> listEvents(@Path("user_id") String userId, @Query("token") String token)
        throws BackendError;

    //********************************************************************************************//
    // Token
    //********************************************************************************************//

    @POST("/token") Token createToken(@Query("facebook_token") String facebook_token) throws BackendError;

    //********************************************************************************************//
    // Tags
    //********************************************************************************************//

    @GET("/tag") List<String> getTagRecommendations() throws BackendError;

    @GET("/tag") List<String> getPopularTags(@Query("x") double x, @Query("y") double y, @Query("max") int max)
        throws BackendError;

    //********************************************************************************************//
    //********************************************************************************************//
}
