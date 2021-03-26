package com.rudyrachman16.githubuserapi.data.models

import com.google.gson.annotations.SerializedName

data class SearchUser(
    val id :Int,
    @SerializedName("login") val username :String,
    @SerializedName("avatar_url") val picUrl :String
)