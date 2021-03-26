package com.rudyrachman16.githubuserapi.data

import com.rudyrachman16.githubuserapi.BuildConfig
import com.rudyrachman16.githubuserapi.data.models.DetailUser
import com.rudyrachman16.githubuserapi.data.models.SearchUser
import com.rudyrachman16.githubuserapi.data.models.Searches
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface APIRequest {

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getSearch(
        @Query("q") search: String,
        @Query("per_page") perPage: Int = 100
    ): Call<Searches>

    @GET("users/{user}")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getDetail(
        @Path("user") user: String
    ): Call<DetailUser>

    @GET("users/{user}/following?per_page=100")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getFollowing(
        @Path("user") user: String
    ): Call<ArrayList<SearchUser>>

    @GET("users/{user}/followers?per_page=100")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getFollowers(
        @Path("user") user: String
    ): Call<ArrayList<SearchUser>>
}