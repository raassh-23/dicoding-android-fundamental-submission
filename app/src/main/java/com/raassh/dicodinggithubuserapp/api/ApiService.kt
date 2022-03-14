package com.raassh.dicodinggithubuserapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchedUsers(
        @Query("q") query: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<ListUsersResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<ListUsersResponse>>
}