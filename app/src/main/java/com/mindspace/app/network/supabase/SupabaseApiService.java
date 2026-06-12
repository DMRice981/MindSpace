package com.mindspace.app.network.supabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupabaseApiService {
    @GET("rest/v1/profiles")
    Call<List<SupabaseDtos.Profile>> findProfilesByUsername(
            @Query("username") String usernameFilter,
            @Query("select") String select,
            @Query("limit") int limit
    );

    @Headers({"Prefer: return=representation"})
    @POST("rest/v1/profiles")
    Call<List<SupabaseDtos.Profile>> createProfile(@Body SupabaseDtos.ProfileInsertRequest request);

    @GET("rest/v1/profiles")
    Call<List<SupabaseDtos.Profile>> findProfileById(
            @Query("id") String idFilter,
            @Query("select") String select,
            @Query("limit") int limit
    );

    @GET("rest/v1/friend_requests")
    Call<List<SupabaseDtos.FriendRequest>> getIncomingRequests(
            @Query("to_user_id") String toUserFilter,
            @Query("status") String statusFilter,
            @Query("select") String select,
            @Query("order") String order
    );

    @GET("rest/v1/friend_requests")
    Call<List<SupabaseDtos.FriendRequest>> findRequestBetweenUsers(
            @Query("or") String orFilter,
            @Query("select") String select,
            @Query("limit") int limit
    );

    @Headers({"Prefer: return=representation"})
    @POST("rest/v1/friend_requests")
    Call<List<SupabaseDtos.FriendRequest>> createFriendRequest(@Body SupabaseDtos.FriendRequestInsertRequest request);

    @PATCH("rest/v1/friend_requests")
    Call<Void> updateFriendRequest(
            @Query("id") String idFilter,
            @Body SupabaseDtos.FriendRequestUpdateRequest request
    );

    @GET("rest/v1/friendships")
    Call<List<SupabaseDtos.Friendship>> getFriends(
            @Query("user_id") String userFilter,
            @Query("select") String select,
            @Query("order") String order
    );

    @GET("rest/v1/friendships")
    Call<List<SupabaseDtos.Friendship>> findFriendship(
            @Query("user_id") String userFilter,
            @Query("friend_id") String friendFilter,
            @Query("select") String select,
            @Query("limit") int limit
    );

    @POST("rest/v1/friendships")
    Call<Void> createFriendship(@Body SupabaseDtos.FriendshipInsertRequest request);

    @DELETE("rest/v1/friendships")
    Call<Void> deleteFriendship(
            @Query("or") String orFilter
    );

    @GET("rest/v1/chat_messages")
    Call<List<SupabaseDtos.ChatMessage>> getChatMessages(
            @Query("or") String orFilter,
            @Query("select") String select,
            @Query("order") String order
    );

    @Headers({"Prefer: return=representation"})
    @POST("rest/v1/chat_messages")
    Call<List<SupabaseDtos.ChatMessage>> sendMessage(@Body SupabaseDtos.ChatMessageInsertRequest request);
}
