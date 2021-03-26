package com.rudyrachman16.githubuserapi.data

import com.rudyrachman16.githubuserapi.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APICall {
    val retrofit = Retrofit.Builder().apply {
        baseUrl(BuildConfig.BASE_URL)
        addConverterFactory(GsonConverterFactory.create())
    }.build()
    val apiReq: APIRequest = retrofit.create(APIRequest::class.java)
}